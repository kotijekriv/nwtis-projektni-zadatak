package org.foi.nwtis.pradic1.biblioteka;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class Ovlast {

    @Getter
    @Setter
    private String korisnik;

    @Getter
    @Setter
    private String podrucje;

    @Getter
    @Setter
    private boolean status;
    
    public Ovlast(){
        
    }

    public Ovlast(String korisnik, String podrucje, boolean status) {
        this.korisnik = korisnik;
        this.podrucje = podrucje;
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.korisnik);
        hash = 79 * hash + Objects.hashCode(this.podrucje);
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
        final Ovlast other = (Ovlast) obj;
        if (!Objects.equals(this.korisnik, other.korisnik)) {
            return false;
        }
        if (!Objects.equals(this.podrucje, other.podrucje)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Ovlast] korisnik: " + korisnik + ", podrucje: " + podrucje + 
                ", status: " + status;
    }
    
}
