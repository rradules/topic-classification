/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import controller.MainController;
import java.util.ArrayList;

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
        document.setInfo(topic);

        return document;
    }

    public Document getDocumentToClassify(String domainName) {
        ArrayList<String> posts = MainController.getInstance().getDocumentForDomainName(domainName);
        document.setDocuments(posts);
        document.setInfo(domainName);

        return document;
    }

    public Document get20Documents(String topic) {
        ArrayList<String> posts = MainController.getInstance().get20DocumentsForTopic(topic);
        document.setDocuments(posts);
        document.setInfo(topic);

        return document;
    }
}
