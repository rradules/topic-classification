/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import java.util.ArrayList;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
//
public class Document {

    ArrayList<String> documents;
    String topic;

    public Document() {
    }

    public ArrayList<String> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<String> documents) {
        this.documents = documents;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (String s : documents) {
            builder.append(s).append(" ");
        }
        return builder.toString().trim();
    }
}
