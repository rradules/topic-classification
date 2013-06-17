/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import controller.MainController;
import java.util.ArrayList;
import model.Blogpost;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DocumentConstructor {

    Document document;

    public DocumentConstructor() {
        document = new Document();
    }

    public Document getDocumentForTopic(String topic) {
        ArrayList<String> posts = MainController.getInstance().getDocumentForTopic(topic);
        document.setDocuments(posts);
        document.setTopic(topic);

        return document;
    }
}
