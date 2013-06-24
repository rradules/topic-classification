/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import functions.FeatureSelection;
import java.util.ArrayList;

/**
 *
 * @author Student
 */
public class Document {

    private ArrayList<String> documents;
    private ArrayList<String> parsedDocuments;
    private String parsedContent;
    private FeatureSelection featureSelection;
    // domain name is the document should be classified
    // topic if the document is for train purposes
    private String info;

    public Document() {
        documents = new ArrayList<>();
        parsedDocuments = new ArrayList<>();
        featureSelection = new FeatureSelection();
    }

//    public ArrayList<String> getDocuments() {
//        return documents;
//    }
    public void setDocuments(ArrayList<String> documents) {
        this.documents = documents;
        parseDocuments();
        parseContent();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<String> getParsedDocuments() {
        return parsedDocuments;
    }

    public String getParsedContent() {
        return parsedContent;
    }

    public void parseContent() {
        StringBuilder builder = new StringBuilder();
        for (String s : parsedDocuments) {
            builder.append(s).append(" ");
        }

        parsedContent = builder.toString().trim();
    }

    public void parseDocuments() {
        parsedDocuments.clear();
        for (String s : documents) {
            String parsed = featureSelection.performFeatureSelection(s);
            parsedDocuments.add(parsed);
        }
    }
}
