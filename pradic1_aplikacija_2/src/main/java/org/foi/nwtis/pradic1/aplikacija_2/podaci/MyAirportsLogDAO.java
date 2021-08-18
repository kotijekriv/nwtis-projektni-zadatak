package org.foi.nwtis.pradic1.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

public class MyAirportsLogDAO {

    public boolean provjeraZapisa(String ident, String datum, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM myairportslog WHERE ident = ? AND flightdate = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, ident);
                s.setString(2, datum);

                ResultSet rs = s.executeQuery();

                if (rs.next()) {
                    return true;
                }

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodajZapis(String ident, String datum, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairportslog (ident, flightdate, `stored`) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, ident);
                s.setString(2, datum);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
