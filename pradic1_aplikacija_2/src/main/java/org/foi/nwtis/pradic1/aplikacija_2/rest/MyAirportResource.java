package org.foi.nwtis.pradic1.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MyAirportsDAO;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;

@Path("mojiAerodromi")
public class MyAirportResource {

    @Inject
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajMojeAerodrome(@HeaderParam("korisnik") String korisnik,
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

        MyAirportsDAO myadao = new MyAirportsDAO();
        List<Aerodrom> aerodromi = myadao.dohvatiKolekcijuPracenihAerodroma(pbp);

        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }

    @Path("{icao}/prate")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnikePoAerodromu(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao) {

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
        
        
        MyAirportsDAO myadao = new MyAirportsDAO();
        List<String> korisniciDohvaceno = myadao.dohvatiKorisnikePretplaćeneNaAerodrom(icao, pbp);

        if (korisniciDohvaceno == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Pogreska pri dohvacanju pretplacenih korisnika.")
                    .build();
        }
        
         List<Korisnik> korisniciPrate = new ArrayList<>();
        for (Korisnik kor : korisnici) {
            if (korisniciDohvaceno.contains(kor.getKorisnik())) {
                korisniciPrate.add(kor);
            }
        }       
                
        return Response
                .status(Response.Status.OK)
                .entity(korisniciPrate)
                .build();
    }

    @Path("{korisnik}/prati")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnikoveAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pkorisnik) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO myadao = new MyAirportsDAO();
        List<Aerodrom> aerodromi = myadao.dohvatiKorisnikoveAerodrome(pkorisnik, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }

    @Path("{korisnik}/prati")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajPracenje(Aerodrom a, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pkorisnik) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO myadao = new MyAirportsDAO();
        Boolean ok = myadao.dodajAerodromZaPratiti(pkorisnik, a.getIcao(), pbp);

        if (ok) {
            return Response
                    .status(Response.Status.OK)
                    .entity(ok)
                    .build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ok)
                    .build();
        }

    }

    @Path("{korisnik}/prati/{icao}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public Response obrisiPracenje(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("korisnik") String pkorisnik, @PathParam("icao") String icao) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MyAirportsDAO myadao = new MyAirportsDAO();
        Boolean ok = myadao.obrisiAerodromZaPratiti(pkorisnik, icao, pbp);

        if (ok) {
            return Response
                    .status(Response.Status.OK)
                    .entity(ok)
                    .build();
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ok)
                    .build();
        }
    }
}
