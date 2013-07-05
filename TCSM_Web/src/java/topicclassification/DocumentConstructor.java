/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

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
    
        public Document getXDocumentsAllTopics(int size) {
          
        ArrayList<String> posts = MainController.getInstance().getXDocumentsofEachTopic(size);
        document.setDocuments(posts);
        document.setInfo("allTopics");

        return document;
    }

    public Document getDocumentToClassify(String domainName) {
        ArrayList<String> posts = MainController.getInstance().getDocumentForDomainName(domainName);
        document.setDocuments(posts);
        document.setInfo(domainName);

        return document;
    }

    public Document getXDocuments(String topic, int size) {
        ArrayList<String> posts = MainController.getInstance().getXDocumentsForTopic(topic,size);
        document.setDocuments(posts);
        document.setInfo(topic);

        return document;
    }
}
