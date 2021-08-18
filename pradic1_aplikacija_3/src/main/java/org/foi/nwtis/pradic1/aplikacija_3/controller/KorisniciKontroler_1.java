package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.AerodromiKlijent_1;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.KorisniciKlijent_1;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.MojiAerodromiKlijent_1;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("korisnik")
@Controller
public class KorisniciKontroler_1 {

    @Inject
    private Models model;

    @Inject
    private HttpSession sesija;

    @Inject
    private ServletContext context;

    @Path("pocetna")
    @GET
    @View("index.jsp")
    public void pocetna() {
    }

    @Path("prijavaKorisnika")
    @GET
    @View("korisnikPrijava.jsp")
    public void prijavaKorisnika() {
    }

    @Path("registracijaKorisnika")
    @GET
    @View("korisnikPodaci.jsp")
    public void registracijaKorisnika() {
    }

    @Path("greska")
    @GET
    @View("greska.jsp")
    public void greska() {
    }

    @Path("administracija")
    @GET
    @View("administracija.jsp")
    public String administracija() {
        if (!autorizirajKorisnika("administracija")) {
            model.put("opisGreske", "Korisnik nema aktivno područje.");
            return "greska.jsp";
        }

        Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
        KorisniciKlijent_1 kk = new KorisniciKlijent_1();
        Object o = kk.dajKorisnike(List.class, k.getKorisnik(), k.getLozinka());

        if (o instanceof List) {
            List<Korisnik> korisnici = (List<Korisnik>) o;
            model.put("korisnici", korisnici);
            return "administracija.jsp";
        } else {
            model.put("opisGreske", (String) o);
            return "greska.jsp";
        }
    }

    @Path("administracijaAerodroma")
    @GET
    @View("administracijaAerodroma.jsp")
    public String administracijaAerodroma() {
        if (!autorizirajKorisnika("administracijaAerodroma")) {
            model.put("opisGreske", "Korisnik nema aktivno područje.");
            return "greska.jsp";
        }

        Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
        MojiAerodromiKlijent_1 mak = new MojiAerodromiKlijent_1();
        AerodromiKlijent_1 ak = new AerodromiKlijent_1();

        Object o = mak.dajMojeAerodrome(List.class, k.getKorisnik(), k.getLozinka());
        Object o1 = ak.dajAerodrome(List.class, null, null, k.getKorisnik(), k.getLozinka());

        if (!(o instanceof List)) {
            model.put("opisGreske", "Nije moguće dohvatiti aktivne aerodrome.");
            return "greska.jsp";
        }

        List<Aerodrom> mojiAerodromi = (List<Aerodrom>) o;
        List<Aerodrom> sviAerodromi = (List<Aerodrom>) o1;
        model.put("mojiAerodromi", mojiAerodromi);
        model.put("sviAerodromi", sviAerodromi);

        return "administracijaAerodroma.jsp";
    }

    @Path("komanda")
    @GET
    @View("komanda.jsp")
    public void komanda() {
    }

    @Path("odjava")
    @GET
    @View("index.jsp")
    public void odjava() {
        if(sesija == null || sesija.getAttribute("sjednica") == null){
            return;
        }
        
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        Sjednica s = (Sjednica) sesija.getAttribute("sjednica");
        StringBuilder sb = new StringBuilder();
        KomandaKlijent kk = new KomandaKlijent(pbp);

        sb.append("LOGOUT ").append(s.getKorisnik()).append(" ").append(s.getId());
        String odgovor = kk.posaljiZahtjev(sb.toString());

        sesija.invalidate();
    }

    @Path("popisKorisnikaPrati")
    @GET
    @View("popisKorisnikaPrati.jsp")
    public void pregledKorisnika() {
        
    }

    private boolean autorizirajKorisnika(String podrucje) {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        Sjednica s = (Sjednica) sesija.getAttribute("sjednica");

        StringBuilder sb = new StringBuilder();
        sb.append("AUTHOR ").append(s.getKorisnik()).append(" ").append(s.getId())
                .append(" ").append(podrucje);

        KomandaKlijent kk = new KomandaKlijent(pbp);
        String odgovor = kk.posaljiZahtjev(sb.toString());

        if (!odgovor.startsWith("OK")) {
            return false;
        }

        return true;
    }

}