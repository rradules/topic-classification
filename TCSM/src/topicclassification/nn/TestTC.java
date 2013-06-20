/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import topicclassification.ml.Document;
import topicclassification.ml.DocumentConstructor;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestTC {

    public static void main(String[] args) throws Throwable {



        DocumentConstructor docCons = new DocumentConstructor();
        Document document = docCons.getDocumentToClassify("nastase.wordpress.com");
        //String content = document.getContent();


        TermFrequency tf = new TermFrequency();
        tf.setDocument(document);
        tf.computeFrequency();

        HashMap<String, Integer> test = tf.getTermfrequency();

        PrintWriter writer = new PrintWriter("nastasewords.txt", "UTF-8");


        Iterator it = test.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
            int val = test.get(key);

            //System.out.println(key + " - " + val);
            //System.out.println(key);
            //writer.println(key);
            writer.println(val);
            //System.out.println(val);
        }

        writer.close();
    }
}
