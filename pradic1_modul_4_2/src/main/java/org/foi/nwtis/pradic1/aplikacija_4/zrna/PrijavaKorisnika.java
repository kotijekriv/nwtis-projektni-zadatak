package org.foi.nwtis.pradic1.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.pradic1.aplikacija_4.sb.OsobniAerodromi;
import org.foi.nwtis.pradic1.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    OsobniAerodromi osobniAerodromi;

    @Getter
    @Setter
    private boolean prijava;

    @Getter
    @Setter
    private boolean izbornik;

    @Getter
    @Setter
    private String korisnik;

    @Getter
    @Setter
    private String lozinka;

    private final PostavkeBazaPodataka pbp;
    private final FacesContext facesContext;
    private final HttpSession session;

    public PrijavaKorisnika() {
        prijava = true;
        izbornik = false;

        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(true);
        pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");
    }

    public String prijaviSe() {

        if (korisnik.isEmpty() || lozinka.isEmpty()) {
            session.setAttribute("opisGreske", "Vazeca sjednica ne postoji za korisnika!");
            return "prijavaNaGresku";
        }

        String odgovor1 = osobniAerodromi.prijavljivanjeKorisnika(korisnik, lozinka, pbp);

        if (!odgovor1.startsWith("OK")) {
            session.setAttribute("opisGreske", odgovor1);
            return "prijavaNaGresku";
        }

        Response odgovor2 = osobniAerodromi.dajKorisnika(korisnik, lozinka, korisnik);

        if (odgovor2.getStatus() != Response.Status.OK.getStatusCode()) {
            session.setAttribute("opisGreske", odgovor2.readEntity(String.class));
            return "prijavaNaGresku";
        }

        String[] razdvojeno = odgovor1.split(" ");
        Sjednica sjednica = new Sjednica();
        sjednica.setId(Integer.parseInt(razdvojeno[1]));
        sjednica.setKorisnik(korisnik);
        sjednica.setVrijemeVrijediDo(Long.parseLong(razdvojeno[2]));
        sjednica.setMaksBrojZahtjeva(Integer.parseInt(razdvojeno[3]));

        Korisnik korisnik1 = odgovor2.readEntity(Korisnik.class);
        korisnik1.setLozinka(lozinka);
        
        session.setAttribute("korisnik", korisnik1);
        session.setAttribute("sjednica", sjednica);

        izbornik = true;
        prijava = false;

        return "";
    }

    public String odjaviSe() {
        if (session.getAttribute("sjednica") == null || session.getAttribute("korisnik") == null) {
            session.setAttribute("opisGreske", "Vazeca sjednica ne postoji za korisnika!");
            return "prijavaNaGresku";
        }

        Korisnik korisnik1 = (Korisnik) session.getAttribute("korisnik");
        Sjednica sjednica = (Sjednica) session.getAttribute("sjednica");

        osobniAerodromi.odjavaKorisnika(korisnik1, sjednica, pbp);
        session.invalidate();

        izbornik = false;
        prijava = true;

        return "prijava";
    }

}
