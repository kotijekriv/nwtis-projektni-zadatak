/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.podaci.MeteoOriginal;

public class MeteoDAO {

    public boolean dodajPodatak(Aerodrom a, MeteoOriginal metorg, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO meteo(ident, lastUpdate, temperatureValue,"
                + " humidityValue, pressureValue, windSpeedValue, windDirectionValue) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                Timestamp ts = new Timestamp(metorg.getDt() * 1000);

                s.setString(1, a.getIcao());
                s.setTimestamp(2, ts);
                s.setFloat(3, metorg.getMainTemp());
                s.setInt(4, metorg.getMainHumidity());
                s.setFloat(5, metorg.getMainPressure());
                s.setFloat(6, metorg.getWindSpeed());
                s.setInt(7, metorg.getWindDeg());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public MeteoOriginal dohvatiPodatak(long vrijeme, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM meteo "
                + "WHERE lastUpdate >= ? AND ident = ? "
                + "ORDER BY lastUpdate ASC LIMIT 1;";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setTimestamp(1, new Timestamp(vrijeme));
                s.setString(2, icao);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    MeteoOriginal metorg = new MeteoOriginal();
                    metorg.setDt(rs.getTimestamp("lastUpdate").getTime());
                    metorg.setIdent(rs.getString("ident"));
                    metorg.setMainTemp(rs.getFloat("temperatureValue"));
                    metorg.setMainHumidity(rs.getInt("humidityValue"));
                    metorg.setMainPressure(rs.getFloat("pressureValue"));
                    metorg.setWindSpeed(rs.getFloat("windSpeedValue"));
                    metorg.setWindDeg(rs.getInt("windDirectionValue"));
                    return metorg;
                }

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<MeteoOriginal> dohvatiSvePodatke(String icao, Timestamp pocetak, Timestamp kraj, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM meteo WHERE lastUpdate >= ? AND lastUpdate <= ? AND ident = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<MeteoOriginal> metPodaci = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setTimestamp(1, pocetak);
                s.setTimestamp(2, kraj);
                s.setString(3, icao);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    MeteoOriginal metorg = new MeteoOriginal();
                    metorg.setDt(rs.getTimestamp("lastUpdate").getTime());
                    metorg.setIdent(rs.getString("ident"));
                    metorg.setMainTemp(rs.getFloat("temperatureValue"));
                    metorg.setMainHumidity(rs.getInt("humidityValue"));
                    metorg.setMainPressure(rs.getFloat("pressureValue"));
                    metorg.setWindSpeed(rs.getFloat("windSpeedValue"));
                    metorg.setWindDeg(rs.getInt("windDirectionValue"));
                    metPodaci.add(metorg);
                }

                return metPodaci;

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
