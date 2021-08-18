package org.foi.nwtis.pradic1.biblioteka;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class Sjednica {


    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String korisnik;

    @Getter
    @Setter
    private long vrijemeKreiranja;

    @Getter
    @Setter
    private long vrijemeVrijediDo;

    @Getter
    @Setter
    private int maksBrojZahtjeva;

    public Sjednica() {

    }

    public Sjednica(int id, String korisnik, long vrijemeKreiranja, long vrijemeVrijediDo,
            int maksBrojZahtjeva) {
        this.id = id;
        this.korisnik = korisnik;
        this.vrijemeKreiranja = vrijemeKreiranja;
        this.vrijemeVrijediDo = vrijemeVrijediDo;
        this.maksBrojZahtjeva = maksBrojZahtjeva;
    }

    @Override
    public String toString() {

        String s = "[Sjednica] id: " + id + ", korisnik: " + korisnik + ", vrijemeKreiranja: "
                + vrijemeKreiranja + ", vrijemeVrijediDo: " + vrijemeVrijediDo
                + ", maksBrojZahtjeva: " + maksBrojZahtjeva;

        return s;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.korisnik);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sjednica other = (Sjednica) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.korisnik, other.korisnik)) {
            return false;
        }
        return true;
    }
}
