/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import topicclassification.ml.Document;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
//Pupose: compute the number of documents in the collection that contain a term t
public class DocumentFrequency {

    private Document document;

    public DocumentFrequency() {
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public int getDocumentFrequency(String word) {
        int freq = 0;
        for (String s : document.getParsedDocuments()) {
            if (s.contains(word.trim())) {
                freq++;
            }
        }
        return freq;
    }

    public double getIDF(String word) {
        int freq = getDocumentFrequency(word);
        int docNo = document.getParsedDocuments().size();
        double idf;
        if (freq == docNo) {
            idf = Math.log(docNo / (docNo - 1));
        } else {
            idf = Math.log(docNo / getDocumentFrequency(word));
        }

        return idf;
    }
}
