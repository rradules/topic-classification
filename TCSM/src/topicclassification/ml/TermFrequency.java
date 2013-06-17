/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
// the number of occurrences of term t in document d (by document we understand the total blog post content extracted from a domain)
public class TermFrequency {

    Document document;
    HashMap<String, Integer> termfrequency;

    public TermFrequency() {
        document = new Document();
        termfrequency = new HashMap<>();
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public HashMap<String, Integer> getTermfrequency() {
        return termfrequency;
    }

    public void setTermfrequency(HashMap<String, Integer> termfrequency) {
        this.termfrequency = termfrequency;
    }

    public void computeFrequency() {
        ArrayList<String> documents = document.getDocuments();
        for (String doc : documents) {
            String[] words = doc.split(" +");
            for (String w : Arrays.asList(words)) {
                Integer num = termfrequency.get(w);
                if (num != null) {
                    termfrequency.put(w, num + 1);
                } else {
                    termfrequency.put(w, 1);
                }
            }
        }
    }
}
