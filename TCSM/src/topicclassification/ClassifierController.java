/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierController {

    private ClassifierFactory classifier;

    public ClassifierController() {
    }

    //possible classifiers:
//    case "NB": Naive Bayes
//    case "NBU": Naive Bayes Updatable 
//    case "CNB": Complement Naive Bayes
//    case "KNN": K Nearest Neighbor
//    case "J48": Decision tree J48
//    case "SMO": Support Vector Machines using Sequential Minimal Optimization    
//    case "MLP" MultiLayer Perceptron
    public String classifyPost(String clsf, String domainName) {
      //  System.out.println("Classifier Factory created");
        classifier = new ClassifierFactory(clsf);

        return classifier.getTopic(domainName);
    }
}
