package org.foi.nwtis.pradic1.aplikacija_3.controller;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("komanda")
@Controller
public class KorisniciKontroler_6 {

    @Inject
    private Models model;
    
    @Inject
    private ServletContext context;

    @FormParam("komanda")
    String komanda;

    @POST
    public String izvrsiKomandu() {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        
        String odgovor = kk.posaljiZahtjev(komanda);
        model.put("odgovor", odgovor);
        
        return "komanda.jsp";
    }

}