package org.foi.nwtis.pradic1.aplikacija_2.dretve;

import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MeteoDAO;
import org.foi.nwtis.pradic1.aplikacija_2.podaci.MyAirportsDAO;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoOriginal;

public class PreuzimanjeMeteoPodataka extends Thread {

    private PostavkeBazaPodataka pbp;
    private boolean krajPreuzimanja = false;
    private long trajanjeCiklusa;
    private OWMKlijent owm;
    private String apiKey;
    private MyAirportsDAO myadao;
    private MeteoDAO meteodao;

    public PreuzimanjeMeteoPodataka(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
    }

    @Override
    public void interrupt() {
        System.out.println("--[PreuzimanjeMeteoPodataka] DRETVA SE ZAUSTAVLJA--");
        this.krajPreuzimanja = true;
        super.interrupt();
    }

    @Override
    public void run() {
        System.out.println("--[PreuzimanjeMeteoPodataka] ZAPOČINJE PREUZIMANJE PODATAKA--");

        if (!(trajanjeCiklusa >= 600 && trajanjeCiklusa <= 3600)) {
            System.out.println("--[PreuzimanjeMeteoPodataka] CIKLUS NIJE U RASPONU OD 10 DO 60 MINUTA--");
            this.interrupt();
        }

        while (!krajPreuzimanja) {
            long pocetakCiklusa = System.currentTimeMillis();

            myadao = new MyAirportsDAO();
            List<Aerodrom> aerodromi = myadao.dohvatiKolekcijuPracenihAerodroma(pbp);
            if (aerodromi != null) {
                preuzimanjeMeteoPodataka(aerodromi);
            }
            long krajCiklusa = System.currentTimeMillis();

            try {
                Thread.sleep(trajanjeCiklusa * 1000 - (krajCiklusa - pocetakCiklusa));
            } catch (InterruptedException ex) {
                System.err.println("--[PreuzimanjeMeteoPodataka] SPAVANJE DRETVE JE POŠLO PO ZLU--");
            }

        }

    }

    @Override
    public synchronized void start() {
        trajanjeCiklusa = Long.parseLong(pbp.dajPostavku("meteo.ciklus"));
        apiKey = pbp.dajPostavku("OpenWeatherMap.apikey");
        owm = new OWMKlijent(apiKey);
        super.start();
    }

    private void preuzimanjeMeteoPodataka(List<Aerodrom> praceniAerodromi) {
        for (Aerodrom a : praceniAerodromi) {
            meteodao = new MeteoDAO();

            String gs = a.getLokacija().getLatitude();
            String gd = a.getLokacija().getLongitude();
            MeteoOriginal metorg = owm.getRealTimeWeatherOriginal(gs, gd);

            if (meteodao.dodajPodatak(a, metorg, pbp)) {
                System.err.println("--[PreuzimanjeMeteoPodataka] Uspjesno dodan meteo podatak za aerodrom:" + a.getIcao() + "--");
            } else {

                System.err.println("--[PreuzimanjeMeteoPodataka] Neuspjesno dodavanje meteo podatak za aerodrom:" + a.getIcao() + "--");
            }

        }

    }

}
