/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import java.util.HashMap;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */

// the number of occurrences of term t in document d (by document we understand the total blog post content extracted from a domain)
public class TermFrequency {


    Document document;
    HashMap<String, Double> termfrequency;

    public TermFrequency() {
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public HashMap<String, Double> getTermfrequency() {
        return termfrequency;
    }

    public void setTermfrequency(HashMap<String, Double> termfrequency) {
        this.termfrequency = termfrequency;
    }
    
//    String[] words=yourtext.split(" ");
//HashMap<String,Integer> frequencies=new HashMap<String,Integer>();
//for (String w: Arrays.asList(words)){
//  Integer num=frequencies.get(w);
//  if (num!=null)
//    frequencies.put(w,num+1);
//  else
//    frequencies.put(w,1);
//}
    
}
