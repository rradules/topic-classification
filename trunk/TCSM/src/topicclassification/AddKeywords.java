/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import controller.MainController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class AddKeywords {

    public static void main(String[] args) throws Throwable {


        String[] topics = {
            "Activism",
            "Business and finance", "Art", "Travel",
            "Gastronomy", "Literature", "Fashion", "Politics", "Religion and spirituality"};

        BufferedReader key = null;
        BufferedReader freq = null;
        BufferedReader tfidf = null;
        for (int i = 0; i < topics.length; i++) {
            try {
                key = new BufferedReader(new FileReader(topics[i] + "_key.txt"));
                freq = new BufferedReader(new FileReader(topics[i] + "_val.txt"));
                tfidf = new BufferedReader(new FileReader(topics[i] + "_val_tfidf.txt"));

                String keyword = key.readLine();
                String aux = freq.readLine();
                String aux2 = tfidf.readLine();
                int frequency;
                double tfval;

                while (keyword != null) {
                    frequency = Integer.parseInt(aux);
                    tfval = Double.parseDouble(aux2);

                    MainController.getInstance().addKeyword(keyword, tfval, frequency, topics[i]);
                    // System.out.println(keyword + " " + frequency + " " + tfval);
                    keyword = key.readLine();
                    aux = freq.readLine();
                    aux2 = tfidf.readLine();
                }

            } catch (IOException ex) {
                Logger.getLogger(AddKeywords.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    key.close();
                    freq.close();
                    tfidf.close();

                } catch (IOException ex) {
                    Logger.getLogger(AddKeywords.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
