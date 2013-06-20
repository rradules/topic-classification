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
public abstract class Document {

    ArrayList<String> documents;
    FeatureSelection featureSelection;

    public Document() {
        documents = new ArrayList<>();
        featureSelection = new FeatureSelection();
    }

    public abstract String getContent();
}
