package org.foi.nwtis.pradic1.zadaca_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

import org.foi.nwtis.pradic1.biblioteka.Ovlast;

public class OvlastiDAO {

    public Ovlast dohvatiOvlasti(String korisnik, String podrucje, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM ovlasti WHERE korisnik = ? AND podrucje = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                s.setString(2, podrucje);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String podrucje1 = rs.getString("podrucje");
                    boolean status = rs.getBoolean("status");

                    Ovlast o = new Ovlast(korisnik1, podrucje1, status);
                    return o;
                }

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Ovlast> dohvatiSveOvlasti(String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM ovlasti WHERE korisnik = ?";
        List<Ovlast> ovlasti = new ArrayList<>();

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String podrucje1 = rs.getString("podrucje");
                    boolean status = rs.getBoolean("status");

                    if (status) {
                        Ovlast o = new Ovlast(korisnik1, podrucje1, status);
                        ovlasti.add(o);
                    }
                }

                return ovlasti;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean azurirajOvlast(Ovlast o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "UPDATE ovlasti SET status = ? WHERE korisnik = ? AND podrucje = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setBoolean(1, o.isStatus());
                s.setString(2, o.getKorisnik());
                s.setString(3, o.getPodrucje());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodajOvlast(Ovlast o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO ovlasti (korisnik, podrucje, status) VALUES (?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, o.getKorisnik());
                s.setString(2, o.getPodrucje());
                s.setBoolean(3, o.isStatus());


                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
