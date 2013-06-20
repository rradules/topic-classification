/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestTC {

    public static void main(String[] args) throws Throwable {
        TermFrequency tf = new TermFrequency();
        String input = "Ana are mere mere si ana are pere";
     //   tf.computeFrequency(input);
        HashMap<String, Integer> test = tf.getTermfrequency();

        Iterator it = test.keySet().iterator();
        
        while(it.hasNext()){
            String key = it.next().toString();
            int val = test.get(key);
            
            System.out.println(key+" - "+val);
        }
    }
}
