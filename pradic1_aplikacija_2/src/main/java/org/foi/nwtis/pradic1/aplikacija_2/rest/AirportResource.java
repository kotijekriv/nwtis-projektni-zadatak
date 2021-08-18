package org.foi.nwtis.pradic1.aplikacija_2.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.AirplanesDAO;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.AirportsDAO;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MeteoDAO;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.MeteoOriginal;

@Path("aerodromi")
public class AirportResource {

    @Inject
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        AirportsDAO adao = new AirportsDAO();
        List<Aerodrom> aerodromi;

        if (drzava != null && !drzava.isEmpty()) {
            aerodromi = adao.filtrirajAerodormeDrzava(drzava, pbp);
        } else if (naziv != null && !naziv.isEmpty()) {
            aerodromi = adao.filtrirajAerodormeNaziv(naziv, pbp);
        } else {
            aerodromi = adao.dohvatiSveAerodrome(pbp);
        }

        return Response
                .status(Response.Status.OK)
                .entity(aerodromi)
                .build();
    }

    @Path("{icao}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiAerodrom(@HeaderParam("korisnik") String korisnik,
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

        AirportsDAO adao = new AirportsDAO();
        Aerodrom aerodrom = adao.dohvatiAerodorm(icao, pbp);
        return Response
                .status(Response.Status.OK)
                .entity(aerodrom)
                .build();
    }

    @Path("{icao}/letovi")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiBrojLetovaAerodroma(@HeaderParam("korisnik") String korisnik,
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

        AirplanesDAO adao = new AirplanesDAO();
        int brojLetova = adao.brojLetova(icao, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(brojLetova)
                .build();
    }

    @Path("{icao}/letovi/{dan}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiLetovaAerodroma(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao, @PathParam("dan") Date dan) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        AirplanesDAO adao = new AirplanesDAO();
        int p = (int) (dohvatiVrijeme(dan, true) / 1000);
        int k = (int) (dohvatiVrijeme(dan, false) / 1000);
        List<AvionLeti> letovi = adao.dohvatiLetove(icao, p, k, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(letovi)
                .build();
    }

    @Path("{icao}/meteoDan/{dan}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiMeteoPodatkePoDatumu(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao, @PathParam("dan") Date dan) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MeteoDAO mdao = new MeteoDAO();
        Timestamp p = new Timestamp(dohvatiVrijeme(dan, true));
        Timestamp k = new Timestamp(dohvatiVrijeme(dan, false));
        List<MeteoOriginal> meteo = mdao.dohvatiSvePodatke(icao, p, k, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(meteo)
                .build();
    }

    @Path("{icao}/meteoVrijeme/{vrijeme}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dohvatiMeteoPodatkePoDatumu(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao, @PathParam("vrijeme") long vrijeme) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KomandaKlijent kk = new KomandaKlijent(pbp);
        String rez = kk.autenticirajKorisnika(korisnik, lozinka);

        if (!rez.startsWith("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Podaci korisnika ne odgovaraju.")
                    .build();
        }

        MeteoDAO mdao = new MeteoDAO();
        MeteoOriginal meteo = mdao.dohvatiPodatak(vrijeme, icao, pbp);

        return Response
                .status(Response.Status.OK)
                .entity(meteo)
                .build();
    }

    private long dohvatiVrijeme(Date dan, boolean pocetak) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dan);

        if (pocetak) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
        }

        return cal.getTimeInMillis();
    }
}