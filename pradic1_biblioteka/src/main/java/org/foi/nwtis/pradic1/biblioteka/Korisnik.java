
package org.foi.nwtis.pradic1.biblioteka;


import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class Korisnik {

    @Getter
    @Setter
    private String korisnik;

    @Getter
    @Setter
    private String lozinka;

    @Getter
    @Setter
    private String prezime;

    @Getter
    @Setter
    private String ime;
    
    public Korisnik(){
        
    }

    public Korisnik(String korisnik, String lozinka, String prezime, String ime) {
        this.korisnik = korisnik;
        this.lozinka = lozinka;
        this.prezime = prezime;
        this.ime = ime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.korisnik);
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
        
        final Korisnik other = (Korisnik) obj;
        if (!Objects.equals(this.korisnik, other.korisnik)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "[Korisnik] korisnik: " + korisnik + ", lozinka: " + lozinka + 
                ", prezime: " + prezime + ", ime: " + ime;
    }
}