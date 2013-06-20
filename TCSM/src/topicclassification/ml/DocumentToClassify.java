/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import functions.FeatureSelection;
import java.util.ArrayList;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
//
public class DocumentToClassify extends Document {

    String domain;

    public DocumentToClassify() {
        super();
    }

    public ArrayList<String> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<String> documents) {
        this.documents = documents;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (String s : documents) {
            String parsed = featureSelection.performFeatureSelection(s);
            builder.append(parsed).append(" ");
        }
        return builder.toString().trim();
    }
}
