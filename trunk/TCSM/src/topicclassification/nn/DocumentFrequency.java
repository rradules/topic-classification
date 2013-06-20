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

    private final int DOCUMENT_NUMBER = 20;
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
        for (String s : document.getDocuments()) {
            if (s.contains(word)) {
                freq++;
            }
        }

        return freq;
    }

    public double getIDF(String word) {
        double idf;
        idf = Math.log(DOCUMENT_NUMBER / (1 + getDocumentFrequency(word)));

        return idf;
    }
}
