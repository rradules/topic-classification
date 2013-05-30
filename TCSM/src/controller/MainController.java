/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Location;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class MainController {

    private static MainController singleton = null;
    private LocationJpaController locationController;
    private EntityManagerFactory emf;

    private MainController() {

        emf = Persistence.createEntityManagerFactory("Crawler1.0PU");
        locationController = new LocationJpaController(emf);
    }

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }

    public Location getLocationByLocation(String name) {
        return locationController.findByLocation(name);
    }

    public void addLocation(String name) throws Exception {
        Location loc = getLocationByLocation(name);

        if (loc == null) {
            Location aux = new Location();
            aux.setLocation(name);
            locationController.create(aux);
        } else {
            throw new Exception("This location already exists in the database.");
        }
    }

}
