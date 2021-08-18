package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.MojiAerodromiKlijent_2;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.MojiAerodromiKlijent_3;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.MojiAerodromiKlijent_4;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;

@Path("aerodrom")
@Controller
public class KorisniciKontroler_5 {

    @Inject
    private Models model;

    @Inject
    private HttpSession sesija;
    
    @Inject
    private KorisniciKontroler_1 kk;

    @FormParam("aerodrom")
    String aerodrom;

    @POST
    @Path("izbrisi")
    public String izbrisiAerodrom() {
        Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
        MojiAerodromiKlijent_4 mak = new MojiAerodromiKlijent_4(k.getKorisnik(), aerodrom);

        Response r = mak.obrisiPracenje(Response.class, k.getKorisnik(), k.getLozinka());
        boolean uspjeh = r.readEntity(Boolean.class);

        if (uspjeh) {
            kk.administracijaAerodroma();
            return "administracijaAerodroma.jsp";
        } else {
            model.put("opisGreske", "Brisanje aerodroma nije uspjelo.");
            return "greska.jsp";
        }
    }

    @POST
    @Path("dodaj")
    public String dodajAerodrom() {
        Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
        MojiAerodromiKlijent_3 mak = new MojiAerodromiKlijent_3(k.getKorisnik());

        Aerodrom a = new Aerodrom();
        a.setIcao(aerodrom);

        Response r = mak.dodajPracenje(a, Response.class, k.getKorisnik(), k.getLozinka());
        boolean uspjeh = r.readEntity(Boolean.class);

        if (uspjeh) {
            kk.administracijaAerodroma();
            return "administracijaAerodroma.jsp";
        } else {
            model.put("opisGreske", "Dodavanje aerodroma nije uspjelo.");
            return "greska.jsp";
        }
    }

    @POST
    @Path("korisnici")
    public String prikaziKorisnike() {
        Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
        MojiAerodromiKlijent_2 mak = new MojiAerodromiKlijent_2(aerodrom);
        Object o = mak.dajKorisnikePoAerodromu(Object.class, k.getKorisnik(), k.getLozinka());

        if (o instanceof List) {
            model.put("korisnici", (List<Korisnik>) o);
            return "popisKorisnikaPrati.jsp";
        } else {
            model.put("opisGreske", (String) o);
            return "greska.jsp";
        }
    }

}