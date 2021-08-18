package org.foi.nwtis.pradic1.aplikacija_2.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class JakartaRestConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        resources.add(KorisniciResource.class);
        resources.add(AirportResource.class);
        resources.add(MyAirportResource.class);
        resources.add(DnevnikResource.class);
        
        return resources;
    }
}
