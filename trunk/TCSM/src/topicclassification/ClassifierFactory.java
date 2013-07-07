/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import functions.DrawPieChart;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierFactory {

    private TextClassifier tc;
    private String classS;
    private DocumentConstructor docCons;
    private DrawPieChart drawChart;
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
            case "LMT":
                classifier = new LMT();
                break;
            case "J48":
                j48 = new J48();
                j48.setUnpruned(true);
                break;
            case "MLP":
                mlp = new MultilayerPerceptron();
                mlp.setHiddenLayers("5, 5, 5");
                //  mlp.setTrainingTime(100);
                mlp.setMomentum(0.7);
                mlp.setLearningRate(0.2);
                break;
            default:
                classifier = new NaiveBayes();
                break;
        }
        //train the text classifier
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
            if (classS.equals("J48")) {
                tc.setClassifier(j48);
            } else {
                tc.setClassifier(classifier);
            }
        }

    }

    public String getTopic(String domainName) {
        getClassifier();
        //  System.out.println("Classifier setup completed");
        double[] result;
        try {
            result = tc.classifyMessage(docCons.getDocumentToClassify(domainName).getParsedContent());
            drawChart = new DrawPieChart(result);
            drawChart.createChartWeka();

            int position = -1;
            double max = 0;
            for (int i = 0; i < result.length; i++) {
                if (result[i] > max) {
                    max = result[i];
                    position = i;
                }
            }
            // System.out.println("Result array: " + Arrays.toString(result));
            return topics[position];
        } catch (Exception ex) {
            Logger.getLogger(ClassifierFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
