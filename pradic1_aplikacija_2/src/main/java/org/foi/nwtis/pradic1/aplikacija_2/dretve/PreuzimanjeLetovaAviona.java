package org.foi.nwtis.pradic1.aplikacija_2.dretve;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.AirplanesDAO;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MyAirportsDAO;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MyAirportsLogDAO;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class PreuzimanjeLetovaAviona extends Thread {

    private PostavkeBazaPodataka pbp;
    private OSKlijent osk;
    private MyAirportsDAO myadao;
    private AirplanesDAO apldao;
    private MyAirportsLogDAO myaldao;
    private int trajanjeCiklusa;
    private int trajanjePauze;
    private Calendar danasnjiDatum;
    private Calendar pocetakPreuz;
    private Calendar krajPreuz;
    private Calendar krajR;
    private String pocetniDatum;
    private String krajnjiDatum;
    private String korisnik;
    private String lozinka;
    private boolean krajPreuzimanja;
    

    public PreuzimanjeLetovaAviona(PostavkeBazaPodataka pbp) {
        this.krajPreuzimanja = false;
        this.pbp = pbp;
        danasnjiDatum = Calendar.getInstance();
        danasnjiDatum.setTimeInMillis(System.currentTimeMillis());
        danasnjiDatum.set(Calendar.HOUR_OF_DAY, 0);
        danasnjiDatum.set(Calendar.MINUTE, 0);
        danasnjiDatum.set(Calendar.SECOND, 0);
        danasnjiDatum.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void interrupt() {
        System.out.println("--[PreuzimanjeLetovaAviona] DRETVA SE ZAUSTAVLJA--");
        this.krajPreuzimanja = true;
        super.interrupt();
    }

    @Override
    public void run() {
        System.out.println("--[PreuzimanjeLetovaAviona] ZAPOČINJE PREUZIMANJE PODATAKA--");
        resetDatuma();

        while (!krajPreuzimanja) {
            long pocetakCiklusa = System.currentTimeMillis();

            myadao = new MyAirportsDAO();
            List<Aerodrom> aerodromi = myadao.dohvatiKolekcijuPracenihAerodroma(pbp);
            if (aerodromi != null) {
                preuzimanjeLetova(aerodromi);
            }

            pocetakPreuz.add(Calendar.DAY_OF_MONTH, 1);
            krajPreuz.add(Calendar.DAY_OF_MONTH, 1);

            long krajCiklusa = System.currentTimeMillis();

            try {
                if (pocetakPreuz.get(Calendar.DAY_OF_MONTH) >= danasnjiDatum.get(Calendar.DAY_OF_MONTH)
                        && pocetakPreuz.get(Calendar.MONTH) >= danasnjiDatum.get(Calendar.MONTH)) {

                    System.out.println("--[PreuzimanjeLetovaAviona] DRETVA UZIMA PAUZU JEDAN DAN--");
                    Thread.sleep(24 * 60 * 60 * 1000 - (krajCiklusa - pocetakCiklusa));

                } else if (krajPreuz.getTimeInMillis() >= krajR.getTimeInMillis()) {
                    resetDatuma();                    
                    long sleepVrijeme = trajanjeCiklusa * 1000 - (krajCiklusa - pocetakCiklusa); 
                    
                    if(sleepVrijeme < 0){
                        sleepVrijeme = 0;
                    }
                    System.out.println("--[PreuzimanjeLetovaAviona] DRETVA POČINJE PONOVNO SA RADOM--");
                    Thread.sleep(sleepVrijeme);
                } else {
                    long sleepVrijeme = trajanjeCiklusa * 1000 - (krajCiklusa - pocetakCiklusa); 
                    
                    if(sleepVrijeme < 0){
                        sleepVrijeme = 0;
                    }
                    System.out.println("--[PreuzimanjeLetovaAviona] DRETVA JE SPREMNA I U IŠČEKIVANJU JE NOVOG CIKLUSA--");
                    Thread.sleep(sleepVrijeme);
                }
            } catch (InterruptedException ex) {
                System.err.println("--[PreuzimanjeLetovaAviona] SPAVANJE DRETVE JE POŠLO PO ZLU--");
            }
        }

        System.out.println("--[PreuzimanjeLetovaAviona] DRETVA SE ZAUSTAVILA--");
    }

    @Override
    public synchronized void start() {
        boolean status = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.status"));
        if (!status) {
            System.out.println("--[PreuzimanjeLetovaAviona] NOVI PODACI SE NE PREUZIMAJU-- ");
            return;
        }
        this.trajanjeCiklusa = Integer.parseInt(pbp.dajPostavku("preuzimanje.ciklus"));
        this.pocetniDatum = pbp.dajPostavku("preuzimanje.pocetak");
        this.krajnjiDatum = pbp.dajPostavku("preuzimanje.kraj");
        this.korisnik = pbp.dajPostavku("OpenSkyNetwork.korisnik");
        this.lozinka = pbp.dajPostavku("OpenSkyNetwork.lozinka");
        this.trajanjePauze = Integer.parseInt(pbp.dajPostavku("preuzimanje.pauza"));

        osk = new OSKlijent(korisnik, lozinka);
        super.start();
    }

    private void preuzimanjeLetova(List<Aerodrom> praceniAerodromi) {

        for (Aerodrom a : praceniAerodromi) {
            try {
                long pocetakCiklusa = System.currentTimeMillis();
                myaldao = new MyAirportsLogDAO();
                apldao = new AirplanesDAO();
                Date date = new Date(pocetakPreuz.getTimeInMillis());
                System.out.println("--[PreuzimanjeLetovaAviona] POČETNI DATUM: " + date.toString() + "--");

                if (!myaldao.provjeraZapisa(a.getIcao(), date.toString(), pbp)) {

                    long pocetak = pocetakPreuz.getTimeInMillis() / 1000;
                    long kraj = krajPreuz.getTimeInMillis() / 1000;
                    List<AvionLeti> letoviAviona = osk.getDepartures(a.getIcao(), pocetak, kraj);

                    for (AvionLeti al : letoviAviona) {
                        if (al.getEstArrivalAirport() != null) {
                            apldao.dodajLet(al, pbp);
                        }
                    }

                    System.out.println("--[PreuzimanjeLetovaAviona] AŽURIRALI SMO LETOVE TA AERODROM: [" + a.getIcao() + "]--");
                    myaldao.dodajZapis(a.getIcao(), date.toString(), pbp);
                    System.out.println("--[PreuzimanjeLetovaAviona] OBAVLJENO JE PREUZIMANJE ZA AERODROM: ["
                            + a.getIcao() + "] "
                                    + "I DATUM: [" + date.toString() + "]--");

                    long krajCiklusa = System.currentTimeMillis();
                    long pauza = (krajCiklusa - pocetakCiklusa) > trajanjePauze
                            ? 0 : trajanjePauze - (krajCiklusa - pocetakCiklusa);

                    Thread.sleep(pauza);

                } else {
                    System.out.println("--[PreuzimanjeLetovaAviona] VEĆ JE OBAVLJENO PREUZIMANJE ZA AERODROM: ["
                            + a.getIcao() + "] "
                                    + "I DATUM: [" + date.toString() + "]--");
                }
            } catch (InterruptedException ex) {
                System.err.println("--[PreuzimanjeLetovaAviona] SPAVANJE DRETVE JE POŠLO PO ZLU--");
            }
        }
    }

    private void resetDatuma() {
        Calendar pocetak;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            pocetak = Calendar.getInstance();
            krajR = Calendar.getInstance();

            pocetak.setTime(sdf.parse(pocetniDatum));
            krajR.setTime(sdf.parse(krajnjiDatum));

            pocetakPreuz = Calendar.getInstance();
            pocetakPreuz.setTimeInMillis(pocetak.getTimeInMillis());
            pocetakPreuz.set(Calendar.HOUR_OF_DAY, 0);
            pocetakPreuz.set(Calendar.MINUTE, 0);
            pocetakPreuz.set(Calendar.SECOND, 0);
            pocetakPreuz.set(Calendar.MILLISECOND, 0);

            krajPreuz = Calendar.getInstance();
            krajPreuz.setTimeInMillis(pocetak.getTimeInMillis());
            krajPreuz.set(Calendar.HOUR_OF_DAY, 23);
            krajPreuz.set(Calendar.MINUTE, 59);
            krajPreuz.set(Calendar.SECOND, 0);
            krajPreuz.set(Calendar.MILLISECOND, 0);
        } catch (java.text.ParseException ex) {
            System.err.println("--[PreuzimanjeLetovaAviona] PRETVARANJE DATUMA JE POŠLO PO ZLU--");
        }
    }

}