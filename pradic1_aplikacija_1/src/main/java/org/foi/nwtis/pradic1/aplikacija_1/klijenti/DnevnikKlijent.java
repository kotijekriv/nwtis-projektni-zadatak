/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.pradic1.aplikacija_1.klijenti;

/**
 * Jersey REST client generated for REST resource:dnevnik [dnevnik/]<br>
 * USAGE:
 * <pre>
 *        DnevnikKlijent client = new DnevnikKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author NWTiS_4
 */
public class DnevnikKlijent {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/pradic1_aplikacija_2/rest/";

    public DnevnikKlijent() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("dnevnik");
    }

    /**
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)
     */
    public <T> T dodajDnevnik(Object requestEntity, Class<T> responseType) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    public void close() {
        client.close();
    }
    
}
