package org.foi.nwtis.pradic1.aplikacija_4.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.KonfiguracijaBP;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

@WebListener
public class SlusacAplikacije implements ServletContextListener {
    
    @Getter
    private static ServletContext servletContext;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.removeAttribute("Postavke");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        
        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF")+File.separator+servletContext.getInitParameter("konfiguracija");
        KonfiguracijaBP konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);
        
        try {
            konfBP.ucitajKonfiguraciju();
            servletContext.setAttribute("Postavke", konfBP);
        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
