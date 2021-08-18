package org.foi.nwtis.pradic1.aplikacija_4.zrna;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import org.foi.nwtis.pradic1.aplikacija_4.sb.OsobniAerodromi;
import org.foi.nwtis.pradic1.aplikacija_4.slusaci.SlusacAplikacije;
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.biblioteka.Sjednica;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

@Named(value = "pregledDnevnik")
@ViewScoped
public class pregledDnevnik implements Serializable {

    @EJB
    OsobniAerodromi osobniAerodromi;
    
    private final Korisnik korisnik;
    private final Sjednica sjednica;

    private final PostavkeBazaPodataka pbp;
    private final FacesContext facesContext;
    private final HttpSession session;

    public pregledDnevnik() {
        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(false);
        pbp = (PostavkeBazaPodataka) SlusacAplikacije.getServletContext().getAttribute("Postavke");

        korisnik = (Korisnik) session.getAttribute("korisnik");
        sjednica = (Sjednica) session.getAttribute("sjednica");
    }

    public String autoriziraj() {
        if (session.getAttribute("sjednica") == null || session.getAttribute("korisnik") == null) {
            session.setAttribute("opisGreske", "Vazeca sjednica ne postoji za korisnika!");
            return "prijavaNaGresku";
        }

        String podrucje = "pregledDnevnik";

        String odgovor = osobniAerodromi.autorizacijaKorisnika(korisnik, sjednica, podrucje, pbp);

        if (!odgovor.startsWith("OK")) {
            session.setAttribute("opisGreske", odgovor);
            return "prijavaNaGresku";
        }

        return "prijavaNaDnevnik";
    }

}
