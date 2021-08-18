package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.foi.nwtis.pradic1.aplikacija_3.podaci.KorisniciKlijent_1;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;

@Path("korisnikRegistracija")
@Controller
public class KorisniciKontroler_2 {

    @Inject
    private Models model;

    @FormParam("ime")
    String ime;

    @FormParam("prezime")
    String prezime;

    @FormParam("korisnik")
    String korisnik;

    @FormParam("lozinka")
    String lozinka;

    @POST
    public String registracijaKorisnika() {
        KorisniciKlijent_1 kk = new KorisniciKlijent_1();
        Korisnik kor = new Korisnik();
        kor.setIme(ime);
        kor.setPrezime(prezime);
        kor.setKorisnik(korisnik);
        kor.setLozinka(lozinka);
        Object o = kk.dodajKorisnika(kor, Korisnik.class);

        if (o instanceof Korisnik) {
            return "korisnikPrijava.jsp";
        } else {
            model.put("opisGreske", "Dogodila se pogreška prilikom registracije korisnika: " + korisnik);
            return "greska.jsp";
        }
    }

}
