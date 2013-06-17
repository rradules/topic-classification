/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.ml;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TextClassifier {

    public static void main(String[] args) {
        try {
            // TextClassifier cl = new TextClassifier(new NaiveBayesMultinomialUpdateable());
            TextClassifier cl = new TextClassifier(new IBk());
            String[] topics = {"Activism", "Business and finance", "Art", "Travel",
                "Gastronomy", "Literature", "Fashion", "Politics", "Religion and spirituality"};

            for (String top : topics) {
                cl.addCategory(top);
            }
            cl.setupAfterCategorysAdded();

            for (String top : topics) {
                cl.addData(top);
            }

            double[] result = cl.classifyMessage("autor carte religie");


            System.out.println("====== RESULT ====== \tCLASSIFIED AS:\t" + topics[cl.getMaxPos(result)]);
            System.out.println(Arrays.toString(result));

        } catch (Exception ex) {
            Logger.getLogger(TextClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private Instances trainingData;
    private StringToWordVector filter;
    private Classifier classifier;
    private boolean upToDate;
    private ArrayList<String> classValues;
    private ArrayList<Attribute> attributes;
    private boolean setup;
    private Instances filteredData;
    private DocumentConstructor docContructor;

    public TextClassifier(Classifier classifier) throws FileNotFoundException {
        filter = new StringToWordVector();
        this.classifier = classifier;
        // Create vector of attributes.
        attributes = new ArrayList<>();
        // Add attribute for holding texts.
        attributes.add(new Attribute("text", (ArrayList) null));
        // Add class attribute.
        classValues = new ArrayList<>();
        setup = false;
        docContructor = new DocumentConstructor();
    }

    public void addCategory(String category) {
        category = category.toLowerCase();
        // if required, double the capacity.
        classValues.add(category);
    }

    public void addData(String topic) throws IllegalStateException {
        if (!setup) {
            throw new IllegalStateException("Must use setup first");
        }
        String post = docContructor.getDocumentForTopic(topic).getContent();
        topic = topic.toLowerCase();
        // Make message into instance.
        Instance instance = makeInstance(post, trainingData);
        // Set class value for instance.
        instance.setClassValue(topic);
        // Add instance to training data.
        trainingData.add(instance);
        upToDate = false;
    }

    /**
     * Check whether classifier and filter are up to date. Build i necessary.
     *
     * @throws Exception
     */
    private void buildIfNeeded() throws Exception {
        if (!upToDate) {
            // Initialize filter and tell it about the input format.
            filter.setInputFormat(trainingData);
            // Generate word counts from the training data.
            filteredData = Filter.useFilter(trainingData, filter);
            // Rebuild classifier.
            classifier.buildClassifier(filteredData);
            upToDate = true;
        }
    }

    public double[] classifyMessage(String message) throws Exception {
        message = message.toLowerCase();
        if (!setup) {
            throw new Exception("Must use setup first");
        }
        // Check whether classifier has been built.
        if (trainingData.numInstances() == 0) {
            throw new Exception("No classifier available.");
        }
        buildIfNeeded();
        Instances testset = trainingData.stringFreeStructure();
        Instance testInstance = makeInstance(message, testset);

        // Filter instance.
        filter.input(testInstance);
        Instance filteredInstance = filter.output();
        return classifier.distributionForInstance(filteredInstance);

    }

    private Instance makeInstance(String text, Instances data) {
        // Create instance of length two.
        Instance instance = new SparseInstance(2);
        // Set value for message attribute
        Attribute messageAtt = data.attribute("text");
        instance.setValue(messageAtt, messageAtt.addStringValue(text));
        // Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }

    public void setupAfterCategorysAdded() {
        attributes.add(new Attribute("Topic", classValues));
        // Create dataset with initial capacity of 100, and set index of class.
        trainingData = new Instances("TextClassificationProblem", attributes, 100);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        setup = true;
    }

    public int getMaxPos(double[] array) {
        int position = -1;
        double max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                position = i;
            }
        }
        return position;
    }
}
