package org.foi.nwtis.pradic1.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.biblioteka.Dnevnik;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

public class DnevnikDAO {

    public boolean dodajDnevnik(Dnevnik d, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO dnevnik (vrijeme, komanda, odgovor, korisnik) "
                + "VALUES (?, ?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setTimestamp(1, new Timestamp(d.getVrijeme()));
                s.setString(2, d.getKomanda());
                s.setString(3, d.getOdgovor());
                s.setString(4, d.getKorisnik());

                int brojAzuriranja = s.executeUpdate();
                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int dohvatiBrojDnevnika(String pKorisnik, long vrijemeOd, long vrijemeDo, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT COUNT(*) as brojDnevnika FROM dnevnik WHERE korisnik = ?";

        if (vrijemeOd != 0 && vrijemeDo != 0) {
            upit += " AND vrijeme >= ? AND vrijeme <= ?";
        }

        try {
            Class.forName(pbp.getDriverDatabase(url));
            int brojac = 0;

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, pKorisnik);

                if (vrijemeOd != 0 && vrijemeDo != 0) {
                    s.setTimestamp(2, new Timestamp(vrijemeOd));
                    s.setTimestamp(3, new Timestamp(vrijemeDo));
                }

                ResultSet rs = s.executeQuery();

                if (rs.next()) {
                    brojac = rs.getInt("brojDnevnika");
                }

                return brojac;

            } catch (SQLException ex) {
                Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public List<Dnevnik> dohvatiDnevnike(String pKorisnik, int pomak, int stranica, long vrijemeOd,
            long vrijemeDo, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM dnevnik WHERE korisnik = ? ";

        if (vrijemeOd != 0 && vrijemeDo != 0) {
            upit += "vrijeme >= ? AND vrijeme <= ? ";
        }

        upit += "LIMIT ? OFFSET ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Dnevnik> dnevnici = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, pKorisnik);

                if (vrijemeOd != 0 && vrijemeDo != 0) {
                    s.setTimestamp(2, new Timestamp(vrijemeOd));
                    s.setTimestamp(3, new Timestamp(vrijemeDo));
                    s.setInt(4, stranica);
                    s.setInt(5, pomak);
                } else {
                    s.setInt(2, stranica);
                    s.setInt(3, pomak);
                }

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    Dnevnik d = new Dnevnik();
                    d.setVrijeme(rs.getTimestamp("vrijeme").getTime());
                    d.setKomanda(rs.getString("komanda"));
                    d.setOdgovor(rs.getString("odgovor"));
                    d.setKorisnik(rs.getString("korisnik"));
                    dnevnici.add(d);
                }

                return dnevnici;

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
