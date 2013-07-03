/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.MainController;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Domain;
import topicclassification.ClassifierController;
import topicclassification.TFIDFClassifier;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TCSM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

//        String url = "http://innerspacejournal.wordpress.com/";
//        if (!url.equals("")) {
//            try {
//                URL verifiedURL = new URL(url);
//                String domName = verifiedURL.getHost();
//                Domain domain = MainController.getInstance().findDomainByName(domName);
//                if (domain == null) {
//                    System.out.println("Adding new domain in the DB");
//                    //Crawler.getInstance().search(verifiedURL.toString(), 6);
//                }
//                ClassifierController classController = new ClassifierController();
//                System.out.println(classController.classifyPost("NB", domName));
//
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(TCSM.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        //      TFIDFClassifier classifier = new TFIDFClassifier();
        //      classifier.classifyBlog(url);
    }
}
