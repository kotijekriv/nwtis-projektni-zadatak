/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.pradic1.biblioteka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.foi.nwtis.pradic1.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

public class KomandaKlijent {

    private final String adresaServerPristupa;
    private final int portServerPristupa;

    public KomandaKlijent(PostavkeBazaPodataka pbp) {
        this.adresaServerPristupa = pbp.dajPostavku("server.pristupa.adresa");
        this.portServerPristupa = Integer.parseInt(pbp.dajPostavku("server.pristupa.port"));
    }

    public String posaljiZahtjev(String zahtjev) {

        StringBuilder odgovor = new StringBuilder();

        try (Socket socket = new Socket(adresaServerPristupa, portServerPristupa);
                InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();) {

            os.write(zahtjev.getBytes("UTF-8"));
            os.flush();
            socket.shutdownOutput();

            while (true) {
                int i = br.read();
                if (i == -1) {
                    break;
                }
                odgovor.append((char) i);
            }

            socket.shutdownInput();
            socket.close();

        } catch (IOException ex) {
            odgovor.append("ERROR 18 Nije moguce se spojiti na server pristupa");
        }

        System.out.println(odgovor);
        return odgovor.toString();
    }

    public String autenticirajKorisnika(String korisnik, String lozinka) {
        StringBuilder sb = new StringBuilder();
        sb.append("AUTHEN ").append(korisnik).append(" ").append(lozinka);
        return this.posaljiZahtjev(sb.toString());
    }
}
