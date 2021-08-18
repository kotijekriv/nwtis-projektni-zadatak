package org.foi.nwtis.pradic1.vjezba_03.konfiguracije;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class KonfiguracijaJSON extends KonfiguracijaApstraktna{

    public KonfiguracijaJSON(String nazivDatoteke) {
        super(nazivDatoteke);
    }
    
    @Override
    public void ucitajKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
         this.obrisiSvePostavke();

        if (nazivDatoteke == null || nazivDatoteke.length() == 0) {
            throw new NeispravnaKonfiguracija("Naziv datoteke nije definiran!");
        }

        File file = new File(nazivDatoteke);

        if (file.exists() && file.isFile()) {
            try ( BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke))) {
                Gson gson = new Gson();
                this.postavke = gson.fromJson(br, Properties.class);
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod učitavanja datoteke: '" + nazivDatoteke + "'!");
            }
        } else {
            throw new NeispravnaKonfiguracija("Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji!");
        }         
    }

    @Override
    public void spremiKonfiguraciju(String nazivDatoteke) throws NeispravnaKonfiguracija {
        if (nazivDatoteke == null || nazivDatoteke.length() == 0) {
            throw new NeispravnaKonfiguracija("Naziv datoteke nije definiran!");
        }

        File file = new File(nazivDatoteke);

        if (!file.exists() || (file.exists() && file.isFile())) {
            try ( FileWriter fw = new FileWriter(nazivDatoteke)) {
                Gson gson = new Gson();
                String json = gson.toJson(this.postavke);
                fw.write(json);
                fw.flush();
            } catch (IOException ex) {
                throw new NeispravnaKonfiguracija("Problem kod učitavanja datoteke: '" + nazivDatoteke + "'!");
            }
        } else {
            throw new NeispravnaKonfiguracija("Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji!");
        }  
    }
}
