/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import crawler.Crawler;
import extractor.BlogpostExtractor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //Crawler.getInstance().search("http://tudorchirila.blogspot.ro/", 20);
        String url = "http://tudorchirila.blogspot.ro/2013/04/perfect-fara-tine-dragoste-de-viata-de.html";
        String url2 = "http://www.manafu.ro/";
      //  Crawler.getInstance().search(url2, 20);
        BlogpostExtractor be = new BlogpostExtractor(url);
        try {
            System.out.println(
                    be.getPostDate(new URL("http://tudorchirila.blogspot.ro/2013/04/23/perfect-fara-tine-dragoste-de-viata-de.html")));
        } catch (MalformedURLException ex) {
            Logger.getLogger(TCSM.class.getName()).log(Level.SEVERE, null, ex);
        }




    }
}
