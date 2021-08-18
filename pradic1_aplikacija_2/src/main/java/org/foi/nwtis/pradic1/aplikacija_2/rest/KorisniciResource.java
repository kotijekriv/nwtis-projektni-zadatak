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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Path("korisnici")
public class KorisniciResource {

    @Inject
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        StringBuilder sb = new StringBuilder();
        String[] pod = rez.split("\\s");
        sb.append("LISTALL ").append(korisnik).append(" ").append(pod[1]);
        rez = kk.posaljiZahtjev(sb.toString());

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Pogreška pri dohvaćanju korisnika.")
                    .build();
        }

        List<Korisnik> korisnici = new ArrayList<>();
        rez = rez.replace("OK ", "");
        rez = rez.replace("\"", "");
        String[] k = rez.split(" ");
        
        for(int i = 0; i < k.length; i++){
            String[] kor;
            kor = k[i].split("\t");
            Korisnik k1 = new Korisnik(kor[0], null, kor[1], kor[2]);
            korisnici.add(k1);
        }
        
        return Response
                .status(Response.Status.OK)
                .entity(korisnici)
                .build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(Korisnik k) {
        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        StringBuilder sb = new StringBuilder();
        
        sb.append("ADD ").append(k.getKorisnik()).append(" ").append(k.getLozinka()).append(" \"")
                .append(k.getPrezime()).append("\" \"").append(k.getIme()).append("\"");
        String rez = kk.posaljiZahtjev(sb.toString());

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Pogreška pri dodavanju novog korisnika.")
                    .build();
        };
        
        return Response
                .status(Response.Status.OK)
                .entity(k)
                .build();

    }

    @Path("{korisnik}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnika(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pKorisnik) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        StringBuilder sb = new StringBuilder();
        String[] pod = rez.split("\\s");
        sb.append("LIST ").append(korisnik).append(" ").append(pod[1]).append(" ").append(pKorisnik);
        rez = kk.posaljiZahtjev(sb.toString());

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Pogreška pri dohvaćanju korisnika.")
                    .build();
        }

        rez = rez.replace("OK ", "");
        rez = rez.replace("\"", "");
        String[] k = rez.split("\t");
        Korisnik odabraniKorisnik = new Korisnik(k[0], null, k[1], k[2]);

        return Response
                .status(Response.Status.OK)
                .entity(odabraniKorisnik)
                .build();
    }

}
