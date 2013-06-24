/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.DecisionStump;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierFactory {

    private TextClassifier tc;
    private String classifier;
    private DocumentConstructor docCons;
    private final String[] topics = {"Activism", "Business and finance", "Art", "Travel",
        "Gastronomy", "Literature", "Fashion", "Personal journal", "Politics", "Religion and spirituality"};

    public ClassifierFactory(String classifier) {
        this.classifier = classifier;
        docCons = new DocumentConstructor();
    }

    public void getClassifier() {
        try {
            switch (classifier) {
                case "NB":
                    tc = new TextClassifier(new NaiveBayes());
                    break;
                case "NBS":
                    tc = new TextClassifier(new NaiveBayesSimple());
                    break;
                case "NBU":
                    tc = new TextClassifier(new NaiveBayesUpdateable());
                    break;
                case "NBM":
                    tc = new TextClassifier(new NaiveBayesMultinomial());
                    break;
                case "NBMU":
                    tc = new TextClassifier(new NaiveBayesMultinomialUpdateable());
                    break;
                case "KNN":
                    tc = new TextClassifier(new IBk());
                    break;
                case "KStar":
                    tc = new TextClassifier(new KStar());
                    break;
                case "DS":
                    tc = new TextClassifier(new DecisionStump());
                    break;
                case "DT":
                    tc = new TextClassifier(new DecisionTable());
                    break;
                default:
                    tc = new TextClassifier(new NaiveBayes());
                    break;
            }

            for (String top : topics) {
                tc.addCategory(top);
            }
            tc.setupAfterCategorysAdded();

            for (String top : topics) {
                tc.addData(top);
            }
        } catch (FileNotFoundException | IllegalStateException ex) {
            Logger.getLogger(ClassifierFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTopic(String domainName) {
        getClassifier();
        System.out.println("Classifier setup completed");
        try {
            double[] result = tc.classifyMessage(docCons.getDocumentToClassify(domainName).getParsedContent());

            int position = -1;
            double max = 0;
            for (int i = 0; i < result.length; i++) {
                if (result[i] > max) {
                    max = result[i];
                    position = i;
                }
            }
            System.out.println("Result array: " + Arrays.toString(result));
            return topics[position];
        } catch (Exception ex) {
            Logger.getLogger(ClassifierFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
