/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Option;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierFactory {

    private TextClassifier tc;
    private String classS;
    private DocumentConstructor docCons;
    private final String[] topics = {"Activism", "Business and finance", "Art", "Travel",
        "Gastronomy", "Literature", "Fashion", "Personal journal", "Politics", "Religion and spirituality"};

    public ClassifierFactory(String classS) {
        this.classS = classS;
        docCons = new DocumentConstructor();
    }

    public void getClassifier() {
        Classifier classifier = null;
        J48 j48 = null;
        MultilayerPerceptron mlp = null;
        try {
            tc = new TextClassifier();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClassifierFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (classS) {
            case "NB":
                classifier = new NaiveBayes();
                break;
            case "NBS":
                classifier = new NaiveBayesSimple();
                break;
            case "NBU":
                classifier = new NaiveBayesUpdateable();
                break;
            case "NBM":
                classifier = new NaiveBayesMultinomial();
                break;
            case "NBMU":
                classifier = new NaiveBayesMultinomialUpdateable();
                break;
            case "KNN":
                classifier = new IBk();
                break;
            case "KStar":
                classifier = new KStar();
                break;
            case "DS":
                classifier = new DecisionStump();
                break;
            case "DT":
                classifier = new DecisionTable();
                break;
            case "DecTree":
                j48 = new J48();
                j48.setUnpruned(true);
            case "MLP":
                mlp = new MultilayerPerceptron();
                mlp.setMomentum(0.7);
                mlp.setLearningRate(0.2);
                break;
            default:
                classifier = new NaiveBayes();
                break;
        }
        //            for (String top : topics) {
        //                tc.addCategory(top);
        //            }
        //            tc.setupAfterCategorysAdded();
        //
        //            for (String top : topics) {
        //                tc.addData(top);
        //            }
        //            tc.buildFile();
        if (classS.equals("MLP")) {
            tc.setClassifier(mlp);
        } else {
            if (classS.equals("DecTree")) {
                tc.setClassifier(j48);
            } else {
                tc.setClassifier(classifier);
            }
        }

    }

    public String getTopic(String domainName) {
        getClassifier();
        System.out.println("Classifier setup completed");
        double[] result;
        try {
            if (classS.equals("DecTree") || classS.equals("DT")) {
                result = tc.classifyMessage(docCons.getDocumentToClassify(domainName).getParsedContent(), true);
            } else {
                result = tc.classifyMessage(docCons.getDocumentToClassify(domainName).getParsedContent(), false);
            }
            //System.out.println("\n\nClassifier model:\n\n" + tc.getClassifier());

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
