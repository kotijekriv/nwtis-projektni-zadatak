package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.KorisniciKlijent_2;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("prijavaKorisnika")
@Controller
public class KorisniciKontroler_3 {

    @Inject
    private Models model;

    @Inject
    private HttpSession sesija;

    @Inject
    private HttpServletRequest zahtjev;

    @Inject
    ServletContext context;

    @FormParam("korisnik")
    String korisnik;

    @FormParam("lozinka")
    String lozinka;

    @POST
    public String prijavaKorisnika() {

        if ((korisnik == null || korisnik.isEmpty()) && (lozinka == null || lozinka.isEmpty())) {
            model.put("opisGreske", "Nisu unešeni korisnički podaci!");
            return "greska.jsp";
        } else if (korisnik == null || korisnik.isEmpty()) {
            model.put("opisGreske", "Niste unjeli korisničko ime!");
            return "greska.jsp";
        } else if (lozinka == null || lozinka.isEmpty()) {
            model.put("opisGreske", "Niste unjeli lozinku!");
            return "greska.jsp";
        }

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");

        KomandaKlijent kk = new KomandaKlijent(pbp);
        String odgovor = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!odgovor.startsWith("OK")) {
            model.put("opisGreske", "Korisnički podaci nisu ispravni.");
            return "greska.jsp";
        }

        KorisniciKlijent_2 kk2 = new KorisniciKlijent_2(korisnik);
        Object o = kk2.dajKorisnika(Korisnik.class, korisnik, lozinka);
        StringBuilder sb = new StringBuilder();
        String[] pod = odgovor.split("\\s");

        if (o instanceof Korisnik) {
            Korisnik k = (Korisnik) o;
            k.setLozinka(lozinka);
            
            Sjednica s = new Sjednica(Integer.parseInt(pod[1]), korisnik, System.currentTimeMillis(), 
                    Long.parseLong(pod[2]), Integer.parseInt(pod[3]));
            
            sesija = zahtjev.getSession(true);
            sesija.setAttribute("korisnik", k);
            sesija.setAttribute("sjednica", s);
            
            return "index.jsp";
            
        } else {
            model.put("opisGreske", "Korisnički podaci nisu ispravni.");
            return "greska.jsp";
        }
    }
}
