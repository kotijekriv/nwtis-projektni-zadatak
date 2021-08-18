package org.foi.nwtis.pradic1.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.aplikacija_4.sb.OsobniAerodromi;
import org.foi.nwtis.pradic1.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.MeteoOriginal;

@Named(value = "pregledAerodroma")
@ViewScoped
public class PregledAerodroma implements Serializable {

    @EJB
    OsobniAerodromi osobniAerodromi;

    @Getter
    @Setter
    private String aerodrom;

    @Getter
    @Setter
    private String letDatum;

    @Getter
    @Setter
    private String vrijemeMeteoPodaci;

    @Getter
    @Setter
    private String datumMeteo;

    @Getter
    private List<MeteoOriginal> meteo = new ArrayList<>();

    @Getter
    private List<AvionLeti> letovi = new ArrayList<>();

    private List<Aerodrom> praceniAerodromi = new ArrayList<>();

    private final Korisnik korisnik;
    private final Sjednica sjednica;
    private final String datumRegex = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d$";
    private final String vrijemeRegex = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

    private final PostavkeBazaPodataka pbp;
    private final FacesContext facesContext;
    private final HttpSession session;

    public PregledAerodroma() {
        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(false);
        pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");

        korisnik = (Korisnik) session.getAttribute("korisnik");
        sjednica = (Sjednica) session.getAttribute("sjednica");
    }

    public List<Aerodrom> getPraceniAerodromi() {
        Response odgovor = osobniAerodromi.dohvatiPraceneAerodrome(korisnik);

        if (odgovor.getStatus() != Response.Status.OK.getStatusCode()) {
            return null;
        }

        praceniAerodromi = odgovor.readEntity(List.class);
        
        return praceniAerodromi;
    }

    public String autoriziraj() {
        if (session.getAttribute("sjednica") == null || session.getAttribute("korisnik") == null) {
            session.setAttribute("opisGreske", "Vazeca sjednica ne postoji za korisnika!");
            return "aerodromiNaGresku";
        }

        String podrucje = "pregledAerodroma";

        String odgovor = osobniAerodromi.autorizacijaKorisnika(korisnik, sjednica, podrucje, pbp);

        if (!odgovor.startsWith("OK")) {
            session.setAttribute("opisGreske", odgovor);
            return "prijavaNaGresku";
        }

        return "prijavaNaAerodrome";
    }

    public String dajLetove() {

        if (letDatum == null || letDatum.isEmpty() || !letDatum.matches(datumRegex)) {
            session.setAttribute("opisGreske", "Pogresan datum leta!");
            return "aerodromiNaGresku";
        }

        Response odgovor = osobniAerodromi.dohvatiLetoveAerodroma(korisnik, aerodrom, letDatum);

        if (odgovor.getStatus() != Response.Status.OK.getStatusCode()) {
            session.setAttribute("opisGreske", odgovor.readEntity(String.class));
            return "aerodromiNaGresku";
        }

        letovi = odgovor.readEntity(List.class);
        return "";
    }

    public String dohvatiMeteo() {

        if (datumMeteo == null || datumMeteo.isEmpty() || !datumMeteo.matches(datumRegex)) {
            session.setAttribute("opisGreske", "Pogresan meteo datum!");
            return "aerodromiNaGresku";
        }

        Response odgovor = osobniAerodromi.dohvatiMeteoPoDanu(korisnik, aerodrom, datumMeteo);

        if (odgovor.getStatus() != Response.Status.OK.getStatusCode()) {
            session.setAttribute("opisGreske", odgovor.readEntity(String.class));
            return "aerodromiNaGresku";
        }
        
        meteo = odgovor.readEntity(List.class);
        return "";
    }

    public String dohvatiVrijeme() {
        try {
            if (vrijemeMeteoPodaci == null || vrijemeMeteoPodaci.isEmpty()
                    || !vrijemeMeteoPodaci.matches(vrijemeRegex)) {
                session.setAttribute("opisGreske", "Pogresno meteo vrijeme!");
                return "aerodromiNaGresku";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            long vrijemeFormat = sdf.parse(vrijemeMeteoPodaci).getTime();

            Response odgovor = osobniAerodromi.dohvatiMeteoPoVremenu(korisnik, aerodrom, String.valueOf(vrijemeFormat));
            if (odgovor.getStatus() != Response.Status.OK.getStatusCode()) {
                session.setAttribute("opisGreske", odgovor.readEntity(String.class));
                return "aerodromiNaGresku";
            }
            MeteoOriginal m = odgovor.readEntity(MeteoOriginal.class);
            meteo.clear();
            meteo.add(m);
        } catch (ParseException ex) {
            session.setAttribute("opisGreske", "Pogresano meteo vrijeme!");
            return "aerodromiNaGresku";
        }
        return "";
    }

    public String pretvorbaUTimestamp(long vrijeme, boolean unix) {
        if (unix) {
            vrijeme *= 1000;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(vrijeme);
        StringBuilder sb = new StringBuilder();

        String dan = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String mjesec = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String godina = String.valueOf(cal.get(Calendar.YEAR));
        String sati = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", cal.get(Calendar.MINUTE));
        String sekunde = String.format("%02d", cal.get(Calendar.SECOND));

        sb.append(dan).append(".").append(mjesec).append(".")
                .append(godina).append(" ").append(sati).append(":")
                .append(minute).append(":").append(sekunde);

        return sb.toString();
    }

}
