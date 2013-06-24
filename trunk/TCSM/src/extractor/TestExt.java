/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import crawler.Crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestExt {
      public static void main(String[] args) {
        // TODO code application logic here

        BufferedReader br = null;
        ArrayList<String> links = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("religie.txt"));
            String line = br.readLine();

            while (line != null) {
                links.add(line);
                String url = "http://" + line;
                //System.out.println(url);
                Crawler.getInstance().search(url, 6);
                line = br.readLine();
            }

            for (String aux : links) {
                MainController.getInstance().addDomainCategory(aux, "Religion and spirituality");
            }


        } catch (IOException ex) {
            Logger.getLogger(TestExt.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TestExt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Crawler.getInstance().search("http://stylediary.wordpress.ro/", 6);

        // System.out.println(MainController.getInstance().findBlogpostByDomain("tudorchirila.blogspot.ro").size());

    }
}
