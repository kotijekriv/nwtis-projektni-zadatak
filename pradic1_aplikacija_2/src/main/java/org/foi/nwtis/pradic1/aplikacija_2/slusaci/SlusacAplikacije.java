package org.foi.nwtis.pradic1.aplikacija_2.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.aplikacija_2.dretve.PreuzimanjeLetovaAviona;
import org.foi.nwtis.pradic1.aplikacija_2.dretve.PreuzimanjeMeteoPodataka;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private PreuzimanjeLetovaAviona pla;
    private PreuzimanjeMeteoPodataka pmp;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (pla != null && pla.isAlive()) {
            pla.interrupt();
        }

        if (pmp != null && pmp.isAlive()) {
            pmp.interrupt();
        }

        ServletContext servletContext = sce.getServletContext();
        servletContext.removeAttribute("Postavke");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF") + File.separator + servletContext.getInitParameter("konfiguracija");
        PostavkeBazaPodataka konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);

        try {
            konfBP.ucitajKonfiguraciju();
            servletContext.setAttribute("Postavke", konfBP);
            pla = new PreuzimanjeLetovaAviona(konfBP);
            pmp = new PreuzimanjeMeteoPodataka(konfBP);
            pla.start();
            pmp.start();
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
