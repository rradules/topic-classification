/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import controller.MainController;
import java.util.ArrayList;
import java.util.List;
import model.Blogpost;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DocumentConstructor {
    
    DocumentForTrain document;
    DocumentToClassify docToClasss;
    
    public DocumentConstructor() {
        document = new DocumentForTrain();
        docToClasss = new DocumentToClassify();
    }
    
    public DocumentForTrain getDocumentForTopic(String topic) {
        ArrayList<String> posts = MainController.getInstance().getDocumentForTopic(topic);
        document.setDocuments(posts);
        document.setTopic(topic);
        
        return document;
    }
    
    public DocumentToClassify getDocumentToClassify(String domainName) {
        ArrayList<String> posts = MainController.getInstance().getDocumentForDomainName(domainName);
        docToClasss.setDocuments(posts);
        docToClasss.setDomain(domainName);
        return docToClasss;
        
    }
}
