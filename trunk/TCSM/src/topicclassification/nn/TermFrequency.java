/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import topicclassification.ml.Document;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
// the number of occurrences of term t in document d (by document we understand the total blog post content extracted from a domain)
public class TermFrequency {

    Document document;
    HashMap<String, Double> termfrequency;
    DocumentFrequency docFreq;

    public TermFrequency() {
        document = new Document();
        termfrequency = new HashMap<>();
        docFreq = new DocumentFrequency();
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
        docFreq.setDocument(document);
    }

    public HashMap<String, Double> getTermfrequency() {
        return termfrequency;
    }

    public void setTermfrequency(HashMap<String, Double> termfrequency) {
        this.termfrequency = termfrequency;
    }

    public void computeFrequency() {
        String content = document.getContent();
        String[] words = content.split(" +");
        for (String w : Arrays.asList(words)) {
            Double num = termfrequency.get(w);
            if (num != null) {
                termfrequency.put(w, num + 1);
            } else {
                termfrequency.put(w, 1.0);
            }
        }
    }

    public double maxFrequency() {
        Collection<Double> vals = termfrequency.values();
        double max = Collections.max(vals);

        return max;
    }

    public void computeAugmentedFrequency() {
        computeFrequency();
        Iterator it = termfrequency.keySet().iterator();
        double max = maxFrequency();

        while (it.hasNext()) {
            String key = it.next().toString();
            double val = termfrequency.get(key);
            double tf = 0.4 + ((0.4 * val) / max);

            termfrequency.put(key, tf);
        }
    }

    public void computeTFIDF() {
        computeFrequency();
        Iterator it = termfrequency.keySet().iterator();
        double max = maxFrequency();

        while (it.hasNext()) {
            String key = it.next().toString();
            double val = termfrequency.get(key);
            double tf = 0.4 + ((0.4 * val) / max);
            double idf = docFreq.getIDF(key);
            
            //System.out.println(key+ " - "+ tf*idf);

            termfrequency.put(key, tf * idf);
        }

    }
}
