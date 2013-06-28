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
//    case "NBS": Naive Bayes Simple!!! nu merge
//    case "NBU": Naive Bayes Updatable 
//    case "NBM": Naive Bayes Multinomial !! rezultate eronate
//    case "NBMU": Naive Bayes Multinomial Updatable !! rezultate eronate
//    case "KNN": K Nearest Neighbor
//    case "KStar": K Star  !! rezultate eronate
//    case "DS": Desicision stump !! rezultate eronate
//    case "DT": Desicision table || Cannot handle string attributes!
//    case "DecTree": Desicision tree J48
// default MLP
    
    public String classifyPost(String clsf, String domainName) {
        System.out.println("Classifier Factory created");
        classifier = new ClassifierFactory(clsf);
            classifier.getClassifier();

        return classifier.getTopic(domainName);
    }
}
