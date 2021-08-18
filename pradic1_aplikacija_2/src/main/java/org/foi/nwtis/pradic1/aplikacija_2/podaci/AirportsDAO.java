package org.foi.nwtis.pradic1.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

public class AirportsDAO {

    public Aerodrom dohvatiAerodorm(String ident, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airports WHERE ident = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, ident);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String naziv = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] kordinate = coordinates.split(",");
                    Lokacija lokacija = new Lokacija(kordinate[1].trim(), kordinate[0].trim());

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    return a;
                }

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public List<Aerodrom> dohvatiSveAerodrome(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airports";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Aerodrom> aerodromi = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String naziv = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] kordinate = coordinates.split(",");
                    Lokacija lokacija = new Lokacija(kordinate[1].trim(), kordinate[0].trim());

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    aerodromi.add(a);
                }
                
                return aerodromi;
                
            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public List<Aerodrom> filtrirajAerodormeNaziv(String naziv, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airports WHERE NAME LIKE ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Aerodrom> aerodromi = new ArrayList<>();
            
            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, naziv);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String nazivQ = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] kordinate = coordinates.split(",");
                    Lokacija lokacija = new Lokacija(kordinate[1].trim(), kordinate[0].trim());
                    
                    Aerodrom a = new Aerodrom(icao, nazivQ, drzava, lokacija);
                    aerodromi.add(a);
                }
                
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public List<Aerodrom> filtrirajAerodormeDrzava(String drzava, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airports WHERE iso_country = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Aerodrom> aerodromi = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, drzava);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String naziv = rs.getString("name");
                    String drzavaQ = rs.getString("iso_country");
                    String coordinates = rs.getString("coordinates");
                    String[] kordinate = coordinates.split(",");
                    Lokacija lokacija = new Lokacija(kordinate[1].trim(), kordinate[0].trim());

                    Aerodrom a = new Aerodrom(icao, naziv, drzavaQ, lokacija);
                    aerodromi.add(a);
                }
                
                return aerodromi;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
