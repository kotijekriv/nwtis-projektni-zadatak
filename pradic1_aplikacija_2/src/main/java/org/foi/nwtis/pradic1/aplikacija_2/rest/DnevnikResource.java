package org.foi.nwtis.pradic1.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.DnevnikDAO;
import org.foi.nwtis.pradic1.biblioteka.Dnevnik;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("dnevnik")
public class DnevnikResource {

    @Inject
    ServletContext context;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajDnevnik(Dnevnik d) {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        DnevnikDAO ddao = new DnevnikDAO();
        boolean uspjeh = ddao.dodajDnevnik(d, pbp);

        if (uspjeh) {
            return Response
                    .status(Response.Status.OK)
                    .entity(d)
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Pogreška pri dodavanju dnevnika.")
                    .build();
        }
    }

    @GET
    @Path("{korisnik}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajDnevnike(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik, 
            @QueryParam("pomak") int pomak, @QueryParam("stranica") int stranica,
            @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        DnevnikDAO ddao = new DnevnikDAO();
        List<Dnevnik> dnevnici = ddao.dohvatiDnevnike(pKorisnik, pomak, stranica, vrijemeOd, 
                vrijemeDo, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(dnevnici)
                .build();
    }

    @GET
    @Path("{korisnik}/broj")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajBrojDnevnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @PathParam("korisnik") String pKorisnik, 
            @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        DnevnikDAO ddao = new DnevnikDAO();
        int brojDnevnika = ddao.dohvatiBrojDnevnika(pKorisnik, vrijemeOd, vrijemeDo, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(brojDnevnika)
                .build();
    }
}