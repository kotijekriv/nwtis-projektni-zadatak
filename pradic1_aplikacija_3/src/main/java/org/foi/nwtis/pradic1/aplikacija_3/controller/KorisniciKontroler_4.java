package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("podrucje")
@Controller
public class KorisniciKontroler_4 {

    @Inject
    private Models model;

    @Inject
    private HttpSession sesija;

    @Inject
    private ServletContext context;

    @FormParam("korisnik")
    String korisnik;

    @FormParam("podrucje")
    String podrucje;

    @POST
    @Path("aktiviraj")
    public String aktivirajPodrucje() {
        return administracijaPodrucja("GRANT");
    }

    @POST
    @Path("deaktiviraj")
    public String deaktivirajPodrucje() {
        return administracijaPodrucja("REVOKE");
    }

    private String administracijaPodrucja(String komanda) {
        Sjednica s = (Sjednica) sesija.getAttribute("sjednica");
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        StringBuilder sb = new StringBuilder();

        sb.append(komanda).append(" ").append(s.getKorisnik()).append(" ").append(s.getId())
                .append(" ").append(podrucje).append(" ").append(korisnik);
        String odgovor = kk.posaljiZahtjev(sb.toString());

        if (!odgovor.startsWith("OK")) {
            model.put("opisGreske", odgovor);
            return "greska.jsp";
        }

        return "administracija.jsp";
    }
}