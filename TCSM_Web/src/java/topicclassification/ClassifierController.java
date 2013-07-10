/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import controller.MainController;
import crawler.Crawler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.TCSM;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierController {

    private ClassifierFactory classifier;

    public ClassifierController() {
    }

    public String classifyPost(String clsf, String url) throws MalformedURLException {
        //  System.out.println("Classifier Factory created");
        classifier = new ClassifierFactory(clsf);
        String domName = null;
        if (!url.equals("")) {

            URL verifiedURL = new URL(url);
            domName = verifiedURL.getHost();
            Domain domain = MainController.getInstance().findDomainByName(domName);
            if (domain == null) {
                System.out.println("Adding new domain in the DB");
                Crawler.getInstance().search(verifiedURL.toString(), 6);
            }

        }

        if (domName != null) {
            return classifier.getTopic(domName);
        }
        return null;
    }
}
