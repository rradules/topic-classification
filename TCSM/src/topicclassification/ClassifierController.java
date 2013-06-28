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
//    case "NBS": Naive Bayes Simple
//    case "NBU": Naive Bayes Updatable
//    case "NBM": Naive Bayes Multinomial
//    case "NBMU": Naive Bayes Multinomial Uodatable
//    case "KNN": K Nearest Neighbor
//    case "KStar": K Star
//    case "DS": Desicision stump
//    case "DT": Desicision table
    public String classifyPost(String clsf, String domainName) {
        System.out.println("Classifier Factory created");
        classifier = new ClassifierFactory(clsf);
        classifier.getClassifier();
       
       // return classifier.getTopic(domainName);
        return "test";
    }
}
