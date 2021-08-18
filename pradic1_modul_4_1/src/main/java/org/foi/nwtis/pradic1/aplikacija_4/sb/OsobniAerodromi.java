package org.foi.nwtis.pradic1.aplikacija_4.sb;

import jakarta.ejb.Stateful;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.AerodromiKlijent_4;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.AerodromiKlijent_5;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.AerodromiKlijent_6;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.KorisniciKlijent_1;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.KorisniciKlijent_2;
import org.foi.nwtis.pradic1.aplikacija_4.podaci.MojiAerodromiKlijent_3;
import org.foi.nwtis.pradic1.biblioteka.KomandaKlijent;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Stateful
public class OsobniAerodromi {

    public String prijavljivanjeKorisnika(String korisnik, String lozinka, PostavkeBazaPodataka pbp) {

        KomandaKlijent komandaKlijent = new KomandaKlijent(pbp);

        return komandaKlijent.autenticirajKorisnika(korisnik, lozinka);
    }

    public Response dajKorisnika(String korisnik, String lozinka, String korisnikTrazi) {
        KorisniciKlijent_2 korisniciKlijent = new KorisniciKlijent_2(korisnikTrazi);

        return korisniciKlijent.dajKorisnika(Response.class, korisnik, lozinka);
    }

    public String odjavaKorisnika(Korisnik k, Sjednica s, PostavkeBazaPodataka pbp) {
        KomandaKlijent komandaKlijent = new KomandaKlijent(pbp);

        return komandaKlijent.posaljiZahtjev("LOGOUT " + k.getKorisnik() + " " + s.getId());
    }

    public String autorizacijaKorisnika(Korisnik k, Sjednica s, String podrucje, PostavkeBazaPodataka pbp) {
        KomandaKlijent komandaKlijent = new KomandaKlijent(pbp);

        return komandaKlijent.posaljiZahtjev("AUTHOR " + k.getKorisnik() + " " + s.getId() + " " + podrucje);
    }

    private String srediDatum(String dan) {
        String[] podaci = dan.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append(podaci[2]).append("-").append(podaci[1]).append("-").append(podaci[0]);

        return sb.toString();
    }

    public Response dohvatiLetoveAerodroma(Korisnik k, String icao, String dan) {
        String datum = srediDatum(dan);
        AerodromiKlijent_4 aerodromiKlijent = new AerodromiKlijent_4(icao, datum);

        return aerodromiKlijent.dohvatiLetovaAerodroma(Response.class, k.getKorisnik(), k.getLozinka());
    }

    public Response dohvatiMeteoPoDanu(Korisnik k, String icao, String dan) {
        String datum = srediDatum(dan);
        AerodromiKlijent_5 aerodromiKlijent = new AerodromiKlijent_5(icao, datum);
        return aerodromiKlijent.dohvatiMeteoPodatkePoDatumu(Response.class, k.getKorisnik(), k.getLozinka());
    }

    public Response dohvatiMeteoPoVremenu(Korisnik k, String icao, String vrijeme) {
        AerodromiKlijent_6 ak = new AerodromiKlijent_6(icao, vrijeme);
        
        return ak.dohvatiMeteoPodatkePoDatumu(Response.class, k.getKorisnik(), k.getLozinka());
    }

    public Response dohvatiPraceneAerodrome(Korisnik k) {
        MojiAerodromiKlijent_3 mak = new MojiAerodromiKlijent_3(k.getKorisnik());
        
        return mak.dajKorisnikoveAerodrome(Response.class, k.getKorisnik(), k.getLozinka());
    }

    public Response dohvatiPopisKorisnika(Korisnik korisnik) {
        KorisniciKlijent_1 korisnikKlijent = new KorisniciKlijent_1();

        return korisnikKlijent.dajKorisnike(Response.class, korisnik.getKorisnik(), korisnik.getLozinka());
    }

}
