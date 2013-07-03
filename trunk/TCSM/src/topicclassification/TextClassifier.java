/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification;

import controller.MainController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Keyword;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TextClassifier {

    private Instances trainingData;
    private StringToWordVector filter;
    private Classifier classifier;
    private boolean upToDate;
    private ArrayList<String> classValues;
    private ArrayList<Attribute> attributes;
    private boolean setup;
    private Instances filteredData;

    public TextClassifier() throws FileNotFoundException {
        filter = new StringToWordVector();
        //  this.classifier = classifier;
        // Create vector of attributes.
        attributes = new ArrayList<>();
        // Add attribute for holding texts.
        attributes.add(new Attribute("text", (ArrayList) null));
        // Add class attribute.
        classValues = new ArrayList<>();
        setup = false;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public void addCategory(String category) {
        category = category.toLowerCase();
        // if required, double the capacity.
        classValues.add(category);
    }

    public void trainNetwork() {
        try {
            ArffLoader loader = new ArffLoader();
            if (classifier.getClass().equals(J48.class)) {
                loader.setFile(new File("trainingSet2.arff"));
                System.out.println("J48 classifier");
            } else if (classifier.getClass().equals(MultilayerPerceptron.class)) {
                loader.setFile(new File("trainingSet4.arff"));
                System.out.println("MLP classifier");
            } else {
                loader.setFile(new File("trainingSet.arff"));
                 System.out.println("General classifier");
            }
            trainingData = loader.getDataSet();
            // setting class attribute
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        } catch (IOException ex) {
            Logger.getLogger(TextClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        upToDate = false;
    }

    public void addData(String topic) throws IllegalStateException {
        if (!setup) {
            throw new IllegalStateException("Must use setup first");
        }
        List<Keyword> trainSet = MainController.getInstance().findKeywordByCategory(topic);
        topic = topic.toLowerCase();
        if (trainSet.size() > 0) {
            for (Keyword keyword : trainSet) {
                // Make message into instance.
                Instance instance = makeInstance(keyword.getKeyword(), trainingData);
                instance.setWeight(keyword.getWeight());
                // Set class value for instance.
                instance.setClassValue(topic);
                // Add instance to training data.
                trainingData.add(instance);
            }
            upToDate = false;
        }
    }

    /**
     * Check whether classifier and filter are up to date. Build if necessary.
     *
     * @throws Exception
     */
    private void buildIfNeeded() throws Exception {
        trainNetwork();
        if (!upToDate) {
            // Initialize filter and tell it about the input format.
            filter.setInputFormat(trainingData);
            // Generate word counts from the training data.
            filteredData = Filter.useFilter(trainingData, filter);
            // Rebuild classifier.
            classifier.buildClassifier(filteredData);
            //  saveModel(classifier, "MLP.txt");
            upToDate = true;
        }
    }

    public double[] classifyMessage(String message) throws Exception {
        message = message.toLowerCase();

        buildIfNeeded();
        System.out.println("Classifier finished bulding.");
        //get a copy of the instance structure.
        Instances testset = trainingData.stringFreeStructure();
        Instance testInstance = makeInstance(message, testset);
        // Filter instance.
        filter.input(testInstance);
        filter.batchFinished();
        Instance filteredInstance = filter.output();

        return classifier.distributionForInstance(filteredInstance);
        // return null;

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
        attributes.add(new Attribute("topic", classValues));
        // Create dataset with initial capacity of 500, and set index of class.
        trainingData = new Instances("TextClassificationProblem", attributes, 5000);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        setup = true;
    }

    public void buildFile(Instance instance, String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(instance.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TextClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(TextClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveModel(Classifier classifier, String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.flush();
            out.close();
            //FileWriter fw = new FileWriter(fileName);
            // fw.
        } catch (Exception e) {
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
}
