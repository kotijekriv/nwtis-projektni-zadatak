package org.foi.nwtis.pradic1.aplikacija_1.dretve;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.aplikacija_1.klijenti.DnevnikKlijent;
import org.foi.nwtis.pradic1.aplikacija_1.podaci.KorisnikDAO;
import org.foi.nwtis.pradic1.biblioteka.Dnevnik;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Ovlast;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.zadaca_2.podaci.OvlastiDAO;

public class ServerPristupa extends Thread {

    private PostavkeBazaPodataka pbp;
    private boolean krajPreuzimanja;
    private ServerSocket ss;
    private int port;
    private int maksCekaca;
    private int maksZahtjeva;
    private int maksDretvi;
    private int brojacDretvi = 0;
    private int sjednicaTrajanje;
    protected int brojacSjednica = 1;
    protected boolean kraj;
    protected HashMap<String, Sjednica> sjednice = new HashMap<>();

    public ServerPristupa(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
        this.krajPreuzimanja = false;
    }

    @Override
    public void interrupt() {
        System.out.println("--[ServerPristupa] DRETVA SE ZAUSTAVLJA--");
        this.krajPreuzimanja = true;
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException ex) {
                System.err.println("ERROR 18 [ServerPristupa] Pogreska pri zaustavljanju "
                        + "Server Socket-a");
            }
        }
    }

    @Override
    public void run() {

        try (ServerSocket socket = new ServerSocket(port)) {
            socket.close();
            System.out.println("--[ServerPristupa] PORT NIJE ZAUZET--");
            pokreniServer();
        } catch (IOException ex) {
            System.err.println("ERROR 18 [ServerPristupa] Port " + port + " je zauzet");
            this.interrupt();
        }
    }

    @Override
    public synchronized void start() {
        System.out.println("--[ServerPristupa] PRIPREMANJE KONFIGURACIJSKIH PODATAKA--");

        try {
            this.port = Integer.parseInt(pbp.dajPostavku("port"));
            this.maksCekaca = Integer.parseInt(pbp.dajPostavku("maks.cekaca"));
            this.maksZahtjeva = Integer.parseInt(pbp.dajPostavku("maks.zahtjeva"));
            this.sjednicaTrajanje = Integer.parseInt(pbp.dajPostavku("sjednica.trajanje"));
            this.maksDretvi = Integer.parseInt(pbp.dajPostavku("maks.dretvi"));
        } catch (NumberFormatException ex) {
            System.err.println("ERROR 18 [ServerPristupa] Neispravam format postavki");
            this.interrupt();
        }

        super.start();
    }

    public void pokreniServer() {

        try {
            ss = new ServerSocket(port, maksCekaca);
            System.out.println("--[ServerPristupa] otvaranje uticnice--");

            while (!kraj) {
                Socket uticnica = ss.accept();

                if (brojacDretvi < maksDretvi) {
                    brojacDretvi++;
                    pokreniDretvuZahtjeva(uticnica);
                } else {
                    OutputStream os = uticnica.getOutputStream();
                    os.write("ERROR 01 Zahtjev je odbačen jer su sve dretve zauzete!".getBytes());
                    os.flush();
                    os.close();
                    uticnica.close();
                }
            }

            ss.close();

        } catch (IOException ex) {
            Logger.getLogger(ServerPristupa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pokreniDretvuZahtjeva(Socket uticnica) {
        DretvaZahtjeva dz = new DretvaZahtjeva("pradic1_" + brojacDretvi, uticnica);
        dz.start();
    }

    /*
        KLASA DRETVE ZAHTJEVA
     */
    private class DretvaZahtjeva extends Thread {

        private Socket uticnica;
        private String korisnik = "";

        public DretvaZahtjeva(String naziv, Socket uticnica) {
            super(naziv);
            this.uticnica = uticnica;

        }

        @Override
        public void run() {
            obradiZahtjev();
        }

        private void obradiZahtjev() {

            try (InputStreamReader isr = new InputStreamReader(uticnica.getInputStream(), "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    OutputStream os = uticnica.getOutputStream()) {

                String poruka;
                long trenutnoVrijeme = System.currentTimeMillis();
                StringBuilder tekst = new StringBuilder();

                while (true) {
                    int i = br.read();
                    if (i == -1) {
                        break;
                    }
                    tekst.append((char) i);
                }

                String zahtjev = tekst.toString();
                System.out.println(zahtjev);
                uticnica.shutdownInput();

                if (provjeriZahtjev(zahtjev)) {
                    poruka = preusmjeravanjeZahtjeva(zahtjev, trenutnoVrijeme);

                } else {
                    poruka = "ERROR 10 [ServerPristupa] Format komande nije ispravan!";
                }

                os.write(poruka.getBytes("UTF-8"));
                os.flush();

                uticnica.shutdownOutput();
                uticnica.close();

                DnevnikKlijent dk = new DnevnikKlijent();
                dk.dodajDnevnik(new Dnevnik(trenutnoVrijeme, poruka, zahtjev, korisnik), Dnevnik.class);

            } catch (IOException ex) {
                System.err.println("[ServerPristupa] Problem pri spajanju na server!");
            } finally {
                brojacDretvi--;
            }
        }

        private String preusmjeravanjeZahtjeva(String zahtjev, long vrijeme) {

            String poruka;
            String[] zahtjevRazdvojeno = zahtjev.split(" ");
            String naredba = zahtjevRazdvojeno[0];
            korisnik = zahtjevRazdvojeno[1];

            switch (naredba) {
                case "ADD":
                    String[] pod = micanjeNavodnika(zahtjev);
                    poruka = dodavanjeKorisnika(pod[0], pod[1], pod[2], pod[3]);
                    return poruka;
                case "AUTHEN":
                    poruka = autentikacijaKorisnika(zahtjevRazdvojeno[1], zahtjevRazdvojeno[2],
                            vrijeme);
                    return poruka;
                case "LOGOUT":
                    poruka = provjeraSjednice(zahtjevRazdvojeno[1],
                            Integer.parseInt(zahtjevRazdvojeno[2]), true);
                    return poruka;
            }

            poruka = provjeraSjednice(zahtjevRazdvojeno[1], Integer.parseInt(zahtjevRazdvojeno[2]),
                    false);
            if (poruka.startsWith("ERROR")) {
                return poruka;
            }

            switch (naredba) {
                case "GRANT":
                    poruka = aktiviranjePodrucjaZaKorisnika(zahtjevRazdvojeno[1],
                            zahtjevRazdvojeno[3], zahtjevRazdvojeno[4]);
                    break;
                case "REVOKE":
                    poruka = deaktiviranjePodrucjaZaKorisnika(zahtjevRazdvojeno[1],
                            zahtjevRazdvojeno[3], zahtjevRazdvojeno[4]);
                    break;
                case "RIGHTS":
                    poruka = vratiListuPodrucjaKorisnikTrazi(zahtjevRazdvojeno[1],
                            zahtjevRazdvojeno[3]);
                    break;
                case "AUTHOR":
                    poruka = autorizacijaKorisnikaZaPodrucje(zahtjevRazdvojeno[1],
                            zahtjevRazdvojeno[3]);
                    break;
                case "LIST":
                    poruka = vratiPodatkeZaKorisnikTrazi(zahtjevRazdvojeno[1], zahtjevRazdvojeno[3]);
                    break;
                case "LISTALL":
                    poruka = vratiPodatkeSvihKorisnika(zahtjevRazdvojeno[1]);
                    break;
                default:
                    poruka = "ERROR 10 [ServerPristupa] Format komande nije ispravan!";
            }

            return poruka;
        }

        private boolean provjeriZahtjev(String zahtjev) {
            String addRegex = "^ADD\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[\\w\\-#!šđčćžŠĐČĆŽ]{1,20}"
                    + "\\s\"[\\w\\-šđčćžŠĐČĆŽ]{1,25}\"\\s\"[\\w\\-šđčćžŠĐČĆŽ]{1,25}\"$";
            String authenRegex = "^AUTHEN\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[\\w\\-#!šđčćžŠĐČĆŽ]{1,20}$";
            String userRegex =  "^(LOGOUT|LISTALL)\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[1-9][0-9]*$";
            String ovlastiRegex ="^(GRANT|REVOKE)\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[1-9][0-9]*\\s"
                    + "[A-Za-z]+\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}$";
            String podrucjeRegex = "^AUTHOR\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[1-9][0-9]*\\s[A-Za-z]+$";
            String traziRegex = "^(LIST|RIGHTS)\\s[\\w\\-šđčćžŠĐČĆŽ]{1,10}\\s[1-9][0-9]*\\s"
                    + "[\\w\\-šđčćžŠĐČĆŽ]{1,10}$";     
                    
            
            return zahtjev.matches(addRegex) || zahtjev.matches(authenRegex)
                    || zahtjev.matches(userRegex) || zahtjev.matches(podrucjeRegex)
                    || zahtjev.matches(traziRegex) || zahtjev.matches(ovlastiRegex);
        }

        private String provjeraSjednice(String korisnik, int idSjednice, boolean odjava) {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = kdao.dohvatiKorisnika(korisnik, "", false, pbp);

            if (k == null) {
                return "ERROR 17 [ServerPristupa] Trazeni korisnik ne postoji: " + korisnik;
            }

            Sjednica sjednica = sjednice.get(k.getKorisnik());

            if (sjednica == null || sjednica.getId() != idSjednice) {
                return "ERROR 15 [ServerPristupa] Korisnik nema vazecu sjednicu: " + korisnik;
            }

            if (odjava) {
                return "OK";
            }

            if (sjednica.getMaksBrojZahtjeva() <= 0) {
                return "ERROR 16 Broj [ServerPristupa] preostalih zahtjeva u sjednici je 0";
            }

            return "OK";
        }

        private String[] micanjeNavodnika(String zahtjev) {
            String[] zahtjevRazdvojeno = zahtjev.split(" ");
            String[] sredeno = new String[4];

            String prezime = zahtjevRazdvojeno[3];
            String ime = zahtjevRazdvojeno[4];
            prezime = prezime.substring(1, prezime.length() - 1);
            ime = ime.substring(1, ime.length() - 1);

            //korisnik
            sredeno[0] = zahtjevRazdvojeno[1];
            //lozinka
            sredeno[1] = zahtjevRazdvojeno[2];
            //prezime
            sredeno[2] = prezime;
            //ime
            sredeno[3] = ime;

            return sredeno;
        }

        private String dodavanjeKorisnika(String korisnik, String lozinka, String prezime,
                String ime) {

            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = new Korisnik(korisnik, lozinka, prezime, ime);

            if (kdao.dohvatiKorisnika(korisnik, lozinka, false, pbp) != null) {
                return "ERROR 18 [ServerPristupa] Uneseni korisnik vec postoji: " + korisnik;
            }

            if (kdao.dodajKorisnika(k, pbp)) {
                return "OK";
            } else {
                return "ERROR 18 [ServerPristupa] Neuspjesno dodavanje korisnika";
            }

        }

        private String autentikacijaKorisnika(String korisnik, String lozinka, long vrijeme) {
            KorisnikDAO kdao = new KorisnikDAO();

            Korisnik k = kdao.dohvatiKorisnika(korisnik, lozinka, true, pbp);

            if (k == null) {
                return "ERROR 11 [ServerPristupa] Korisnik ili lozinka ne odgovaraju ";
            }

            String korime = k.getKorisnik();
            Sjednica sjednica = sjednice.get(korime);
            long vrijemeTrajanja = sjednicaTrajanje + vrijeme;

            if (sjednica != null) {
                sjednica.setVrijemeVrijediDo(vrijemeTrajanja);
            } else {
                sjednica = new Sjednica(brojacSjednica, korime, vrijeme, vrijemeTrajanja, maksZahtjeva);

                if (sjednice.put(sjednica.getKorisnik(), sjednica) != null) {
                    return "ERROR 18 [ServerPristupa] Neuspjesno dodavanje sjednice!";
                }

                brojacSjednica++;
            }

            return "OK " + sjednica.getId() + " " + sjednica.getVrijemeVrijediDo() + " "
                    + sjednica.getMaksBrojZahtjeva();

        }

        private String aktiviranjePodrucjaZaKorisnika(String korisnik, String podrucje,
                String korisnikTrazi) {

            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);
            boolean provjera = false;

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            OvlastiDAO odao = new OvlastiDAO();
            Ovlast ovlast = odao.dohvatiOvlasti(korisnikTrazi, podrucje, pbp);

            if (ovlast == null) {
                ovlast = new Ovlast(korisnikTrazi, podrucje, true);
                provjera = odao.dodajOvlast(ovlast, pbp);
            } else if (ovlast.isStatus() == true) {
                return "ERROR 13 [ServerPristupa] Korisnik:" + korisnikTrazi
                        + " već ima aktivno podrucje: " + podrucje;
            } else if (ovlast.isStatus() == false) {
                ovlast.setStatus(true);
                provjera = odao.azurirajOvlast(ovlast, pbp);
            }

            if (!provjera) {
                return "ERROR 18 [ServerPristupa] Pogreška u dodavanju ili azuriranju pdorjucja";
            }

            return "OK";
        }

        private String deaktiviranjePodrucjaZaKorisnika(String korisnik, String podrucje,
                String korisnikTrazi) {

            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);
            boolean provjera = false;

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            OvlastiDAO odao = new OvlastiDAO();
            Ovlast ovlast = odao.dohvatiOvlasti(korisnikTrazi, podrucje, pbp);

            if (ovlast == null || ovlast.isStatus() == false) {
                return "ERROR 14 [ServerPristupa] Korisnik:" + korisnikTrazi
                        + " nema aktivno podrucje: " + podrucje;
            }

            if (ovlast.isStatus() == true) {
                ovlast.setStatus(false);
                provjera = odao.azurirajOvlast(ovlast, pbp);
            }

            if (!provjera) {
                return "ERROR 18 [ServerPristupa] Pogreška u dodavanju ili azuriranju podrjucja";
            }

            return "OK";
        }

        private String vratiListuPodrucjaKorisnikTrazi(String korisnik, String korisnikTrazi) {
            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            OvlastiDAO odao = new OvlastiDAO();
            List<Ovlast> ovlasti = odao.dohvatiSveOvlasti(korisnikTrazi, pbp);

            if (ovlasti == null || ovlasti.isEmpty()) {
                return "ERROR 18 [ServerPristupa] Korisnik nema niti jedno aktivno podrjucje: "
                        + korisnik;
            }

            StringBuilder odgovor = new StringBuilder();
            odgovor.append("OK");

            for (Ovlast o : ovlasti) {
                odgovor.append(" ");
                odgovor.append(o.getPodrucje());
            }

            return odgovor.toString();
        }

        private String autorizacijaKorisnikaZaPodrucje(String korisnik, String podrucje) {
            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            OvlastiDAO odao = new OvlastiDAO();
            Ovlast ovlast = odao.dohvatiOvlasti(korisnik, podrucje, pbp);

            if (ovlast == null || ovlast.isStatus() == false) {
                return "ERROR 14 [ServerPristupa] Korisnik:" + korisnik + " nema aktivno podrucje: "
                        + podrucje;
            }

            return "OK";
        }

        private String vratiPodatkeZaKorisnikTrazi(String korisnik, String korisnikTrazi) {
            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = kdao.dohvatiKorisnika(korisnikTrazi, "", false, pbp);

            if (k == null) {
                return "ERROR 17 [ServerPristupa] Trazenog korisnika nema: " + korisnik;
            }

            String korisnik1 = k.getKorisnik();
            String ime1 = k.getIme();
            String prezime1 = k.getPrezime();
            String odgovor;

            odgovor = "OK \"" + korisnik1 + "\t" + prezime1 + "\t" + ime1 + "\"";

            return odgovor;
        }

        private String vratiPodatkeSvihKorisnika(String korisnik) {
            Sjednica sjednica = sjednice.get(korisnik);
            sjednica.setMaksBrojZahtjeva(sjednica.getMaksBrojZahtjeva() - 1);

            if (sjednica.getMaksBrojZahtjeva() < 1) {
                return "ERROR 16 [ServerPristupa] Broj preostalih zahtjeva u sjednici je 0";
            }

            KorisnikDAO kdao = new KorisnikDAO();
            List<Korisnik> korisnici = kdao.dohvatiSveKorisnike(pbp);

            StringBuilder odgovor = new StringBuilder();
            odgovor.append("OK");

            for (Korisnik k : korisnici) {
                odgovor.append(" \"").append(k.getKorisnik()).append("\t");
                odgovor.append(k.getPrezime()).append("\t");
                odgovor.append(k.getIme()).append("\"");
            }

            return odgovor.toString();
        }
    }
}
