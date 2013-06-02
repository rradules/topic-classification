/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import model.Location;

/**
 *
 * @author Student
 */
public class LocationExtractor extends AbstractExtractor {

    public LocationExtractor(String url) {
        super(url);
    }

    @Override
    public Object getData() {

        //eliminate "/" from the end of the link
        if (url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }
        //split the link 
        String[] aux = url.split("\\.");
        //build the suffix from the last 2 elements
        String suf = aux[aux.length - 2] + "." + aux[aux.length - 1];

        Location loc = MainController.getInstance().findLocationBySuffix(suf);
        //if the location is not found, rebuild suffix from the last element
        if (loc == null) {
            suf = "." + aux[aux.length - 1];
            loc = MainController.getInstance().findLocationBySuffix(suf);
        }
        return loc;
    }
}
