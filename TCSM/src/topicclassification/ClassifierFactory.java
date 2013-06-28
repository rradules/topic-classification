/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.j48.NBTreeClassifierTree;
import weka.classifiers.trees.j48.NBTreeModelSelection;
import weka.classifiers.trees.m5.RuleNode;
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
            case "NBU":
                classifier = new NaiveBayesUpdateable();
                break;
            case "KNN":
                classifier = new IBk();
                break;
            case "CNB":
                classifier = new ComplementNaiveBayes();
                break;
            case "SMO":
                classifier = new SMO();
                break;
            case "DecTree":
                j48 = new J48();
                j48.setUnpruned(true);
                break;
            case "MLP":
                mlp = new MultilayerPerceptron();
                mlp.setHiddenLayers("5, 5");
                mlp.setTrainingTime(100);
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

    public void saveModel(Classifier classifier, String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
        } catch (IOException e) {
            System.out.println("Error when writing the classifier");
        }
    }

    public Object loadModel(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
            in.close();
            return tmp;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public String getTopic(String domainName) {
        getClassifier();
        System.out.println("Classifier setup completed");
        double[] result;
        try {
            result = tc.classifyMessage(docCons.getDocumentToClassify(domainName).getParsedContent());

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
