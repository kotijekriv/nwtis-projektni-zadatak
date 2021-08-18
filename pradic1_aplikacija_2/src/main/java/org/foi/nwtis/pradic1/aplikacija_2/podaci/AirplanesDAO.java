package org.foi.nwtis.pradic1.aplikacija_2.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class AirplanesDAO {

    public boolean dodajLet(AvionLeti al, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO airplanes (icao24, firstSeen, estDepartureAirport, lastSeen,"
                + " estArrivalAirport, callsign, estDepartureAirportHorizDistance,"
                + "estDepartureAirportVertDistance, estArrivalAirportHorizDistance, "
                + "estArrivalAirportVertDistance, departureAirportCandidatesCount, "
                + "arrivalAirportCandidatesCount, `stored`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, al.getIcao24());
                s.setInt(2, al.getFirstSeen());
                s.setString(3, al.getEstDepartureAirport());
                s.setInt(4, al.getLastSeen());
                s.setString(5, al.getEstArrivalAirport());
                s.setString(6, al.getCallsign());
                s.setInt(7, al.getEstDepartureAirportHorizDistance());
                s.setInt(8, al.getEstDepartureAirportVertDistance());
                s.setInt(9, al.getEstArrivalAirportHorizDistance());
                s.setInt(10, al.getEstArrivalAirportVertDistance());
                s.setInt(11, al.getDepartureAirportCandidatesCount());
                s.setInt(12, al.getArrivalAirportCandidatesCount());

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

    public int brojLetova(String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT COUNT(*) AS brojLetova FROM airplanes WHERE ESTDEPARTUREAIRPORT = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            int brojac = 0;

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);

                ResultSet rs = s.executeQuery();

                if (rs.next()) {
                    brojac = rs.getInt("brojLetova");
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

    public List<AvionLeti> dohvatiLetove(String icao, int pocetak, int kraj, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM airplanes "
                + "WHERE estDepartureAirport = ? AND firstSeen >= ? AND firstSeen <= ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<AvionLeti> letovi = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setInt(2, pocetak);
                s.setInt(3, kraj);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    AvionLeti let = new AvionLeti();

                    let.setIcao24(rs.getString("icao24"));
                    let.setFirstSeen(rs.getInt("firstSeen"));
                    let.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    let.setLastSeen(rs.getInt("lastSeen"));
                    let.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    let.setCallsign(rs.getString("callsign"));
                    let.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    let.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    let.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    let.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    let.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    let.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));

                    letovi.add(let);
                }

                return letovi;

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
