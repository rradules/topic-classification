/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import topicclassification.ml.Document;
import topicclassification.ml.DocumentConstructor;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestTC {

    public static void main(String[] args) throws Throwable {



        DocumentConstructor docCons = new DocumentConstructor();
        String[] topics = {"Activism", "Business and finance", "Art", "Travel",
            "Gastronomy", "Literature", "Fashion", "Personal journal", "Politics", "Religion and spirituality"};

        for (int i = 0; i < topics.length; i++) {


            Document document = docCons.get20Documents(topics[i]);
            //String content = document.getContent();


            TermFrequency tf = new TermFrequency();
            tf.setDocument(document);
            tf.computeFrequency();
            //tf.computeAugmentedFrequency();
            //tf.computeTFIDF();

            HashMap<String, Double> test = tf.getTermfrequency();

            PrintWriter writer = new PrintWriter(topics[i] + "_key.txt", "UTF-8");
            PrintWriter writer2 = new PrintWriter(topics[i] + "_val.txt", "UTF-8");


            Iterator it = test.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next().toString();


                double val = test.get(key);
                DecimalFormat twoDForm = new DecimalFormat("#.######");

                writer.println(key);
                writer2.println(Double.valueOf(twoDForm.format(val)));

            }
            writer.close();
            writer2.close();
        }
    }
}
