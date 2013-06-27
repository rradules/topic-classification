/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import topicclassification.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
        //docFreq = new DocumentFrequency();
    }

    public void setDocFreq(DocumentFrequency docFreq) {
        this.docFreq = docFreq;
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

    public void computeFrequency() {
        String content = document.getParsedContent();
        String[] words = content.split("\\s+");
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

    public void computeLogFrequency() {
        computeFrequency();
        Iterator it = termfrequency.keySet().iterator();
        //double max = maxFrequency();

        while (it.hasNext()) {
            String key = it.next().toString();
            double val = termfrequency.get(key);
            // double tf = 0.5 + ((0.5 * val) / max);
            //double tf = val / max;
            double tf = Math.log(val + 1);
            //double tf = 0.4 + ((0.4 * val) / max);

            termfrequency.put(key, tf);
        }
    }

    public void computeTFIDF() {
        computeLogFrequency();
        Iterator it = termfrequency.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next().toString();
            double tf = termfrequency.get(key);
            double idf = docFreq.getIDF(key);

            termfrequency.put(key, tf * idf);
        }

    }
}
