/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import functions.AccentElim;
import functions.FeatureSelection;
import java.util.ArrayList;

/**
 *
 * @author Student
 */
public class Document {

    private ArrayList<String> documents;
    private FeatureSelection featureSelection;
    private AccentElim accentElim;
    // domain name is the document should be classified
    // topic if the document is for train purposes
    private String info;

    public Document() {
        documents = new ArrayList<>();
        featureSelection = new FeatureSelection();
        accentElim = new AccentElim();
    }

    public ArrayList<String> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<String> documents) {
        this.documents = documents;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        for (String s : documents) {
            if (counter < 10) {
                String parsed = featureSelection.performFeatureSelection(s);
                builder.append(parsed).append(" ");
                counter++;
            }
        }
        //System.out.println(builder.toString().trim());
        return builder.toString().trim();
    }
}
