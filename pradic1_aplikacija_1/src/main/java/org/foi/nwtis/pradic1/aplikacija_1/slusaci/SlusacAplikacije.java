/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.pradic1.aplikacija_1.slusaci;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.aplikacija_1.dretve.ServerPristupa;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.pradic1.vjezba_03.konfiguracije.NeispravnaKonfiguracija;

/**
 *
 * @author pradic1
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ServerPristupa srvPri;

    @Override
    public void contextDestroyed(ServletContextEvent scEvent) {

        if (srvPri != null && (srvPri.isAlive() || srvPri.isDaemon())) {
            srvPri.interrupt();
        }
        ServletContext servletContext = scEvent.getServletContext();
        servletContext.removeAttribute("Postavke");
    }

    @Override
    public void contextInitialized(ServletContextEvent scEvent) {
        ServletContext servletContext = scEvent.getServletContext();

        String putanjaKonfDatoteke = servletContext.getRealPath("WEB-INF") + File.separator + servletContext.getInitParameter("konfiguracija");
        PostavkeBazaPodataka konfBP = new PostavkeBazaPodataka(putanjaKonfDatoteke);

        try {
            konfBP.ucitajKonfiguraciju();
            servletContext.setAttribute("Postavke", konfBP);
            srvPri = new ServerPristupa(konfBP);

            srvPri.start();

        } catch (NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
