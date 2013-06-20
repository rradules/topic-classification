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
        Document document = docCons.get20Documents("Activism");
        //String content = document.getContent();


        TermFrequency tf = new TermFrequency();
        tf.setDocument(document);
        //tf.computeFrequency();
        //tf.computeAugmentedFrequency();
        tf.computeTFIDF();

        HashMap<String, Double> test = tf.getTermfrequency();

        PrintWriter writer = new PrintWriter("keywords_key.txt", "UTF-8");
        PrintWriter writer2 = new PrintWriter("keywords_val.txt", "UTF-8");


        Iterator it = test.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();


            double val = test.get(key);
            DecimalFormat twoDForm = new DecimalFormat("#.######");


            //System.out.println(key + " - " + val);
            //System.out.println(key);
            writer.println(key);
            writer2.println(Double.valueOf(twoDForm.format(val)));
            //writer.println(val);
            //System.out.println(val);
        }



        writer.close();
        writer2.close();
    }
}
