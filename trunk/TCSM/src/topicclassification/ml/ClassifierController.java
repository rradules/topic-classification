/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierController {

    private ClassifierFactory classifier;

    public ClassifierController() {
    }

    public String classifyPost(String clsf, String content) {
        classifier = new ClassifierFactory(clsf);
        return classifier.getTopic(content);
    }
}

