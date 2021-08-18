package org.foi.nwtis.pradic1.biblioteka;

import lombok.Getter;
import lombok.Setter;

public class Dnevnik {

    @Getter
    @Setter
    private long vrijeme;

    @Getter
    @Setter
    private String odgovor;

    @Getter
    @Setter
    private String komanda;

    @Getter
    @Setter
    private String korisnik;

    public Dnevnik() {
    }

    public Dnevnik(long vrijeme, String odgovor, String komanda, String korisnik) {
        this.vrijeme = vrijeme;
        this.odgovor = odgovor;
        this.komanda = komanda;
        this.korisnik = korisnik;
    }

    @Override
    public String toString() {
        return "Dnevnik{" + "vrijeme=" + vrijeme + ", odgovor=" + odgovor + ", "
                + "komanda=" + komanda + ", korisnik=" + korisnik + '}';
    }
    
}