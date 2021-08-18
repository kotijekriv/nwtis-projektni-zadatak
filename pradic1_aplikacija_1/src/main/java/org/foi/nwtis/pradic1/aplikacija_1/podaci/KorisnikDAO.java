package org.foi.nwtis.pradic1.aplikacija_1.podaci;

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
import org.foi.nwtis.pradic1.biblioteka.Korisnik;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

/**
 *
 * @author pradic1
 */

public class KorisnikDAO {

    public Korisnik dohvatiKorisnika(String korisnik, String lozinka, Boolean prijava, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT ime, prezime, korisnik, lozinka FROM korisnici WHERE korisnik = ?";

        if (prijava) {
            upit += " and lozinka = ?";
        }

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                if (prijava) {
                    s.setString(2, lozinka);
                }
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");

                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime);
                    return k;
                }

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Korisnik> dohvatiSveKorisnike(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM korisnici";
        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<Korisnik> korisnici = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String ime = rs.getString("ime");
                    String prezime = rs.getString("prezime");
                    
                    Korisnik k = new Korisnik(korisnik1, "******", prezime, ime);
                    korisnici.add(k);
                }
                return korisnici;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean dodajKorisnika(Korisnik k, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO korisnici (ime, prezime, korisnik, lozinka) VALUES (?, ?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, k.getIme());
                s.setString(2, k.getPrezime());
                s.setString(3, k.getKorisnik());
                s.setString(4, k.getLozinka());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}

