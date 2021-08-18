package org.foi.nwtis.pradic1.aplikacija_2.podaci;

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
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

public class MyAirportsDAO {

    public List<Aerodrom> dohvatiKolekcijuPracenihAerodroma(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT DISTINCT myair.ident, air.NAME, air.iso_country, air.coordinates "
                + "FROM myairports as myair LEFT JOIN airports as air ON myair.ident = air.ident";

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
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<String> dohvatiKorisnikePretplaćeneNaAerodrom(String ident, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT USERNAME FROM myairports WHERE IDENT = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<String> korisnici = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, ident);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik = rs.getString("username");
                    korisnici.add(korisnik);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Aerodrom> dohvatiKorisnikoveAerodrome(String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT air.* "
                + "FROM airports as air LEFT JOIN myairports as myair "
                + "ON air.IDENT = myair.IDENT "
                + "WHERE myair.USERNAME = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Aerodrom> aerodromi = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);

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

    public boolean dodajAerodromZaPratiti(String korisnik, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO myairports (IDENT, USERNAME, `STORED`) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, korisnik);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean obrisiAerodromZaPratiti(String korisnik, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "DELETE FROM myairports WHERE IDENT = ? AND USERNAME = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, korisnik);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
