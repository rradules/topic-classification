/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import functions.DrawPieChart;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import topicclassification.ClassifierController;
import topicclassification.TFIDFClassifier;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ClassifierBean implements java.io.Serializable {

    private String URL;
    private String image;
    private String result;
    private List<String> classifiers = new ArrayList<>();
    private String[] clsf = {"Naive Bayes", "Naive Bayes Updatable",
        "Complement Naive Bayes", "K Nearest Neighbor", "Decision tree C4.5",
        "SVM with Sequential Minimal Optimization", "TF-IDF", "MultiLayer Perceptron"};
    private String chosenClsf;
    private TFIDFClassifier myClassifier = new TFIDFClassifier();
    private ClassifierController clsfController = new ClassifierController();
    private DrawPieChart chart;

    public ClassifierBean() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getChosenClsf() {
        return chosenClsf;
    }

    public void setChosenClsf(String chosenClsf) {
        this.chosenClsf = chosenClsf;
    }

    public List<String> getClassifiers() {
        return classifiers;
    }

    public String[] getClsf() {
        return clsf;
    }

    public void setClsf(String[] clsf) {
        this.clsf = clsf;
    }

    public void setClassifiers(List<String> classifiers) {
        this.classifiers = classifiers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void classifyBlog() {
        String c = null;
        URL = removeWwwFromUrl(URL);
        switch (chosenClsf) {
            case "TF-IDF":
                result = myClassifier.classifyBlog(URL);
            case "Naive Bayes":
                c = "NB";
                break;
            case "Naive Bayes Updatable":
                c = "NBU";
                break;
            case "Complement Naive Bayes":
                c = "CNB";
                break;
            case "K Nearest Neighbor":
                c = "KNN";
                break;
            case "Decision tree C4.5":
                c = "J48";
                break;
            case "SVM with Sequential Minimal Optimization":
                c = "SMO";
                break;
            case "MultiLayer Perceptron":
                c = "MLP";
                break;
            default:
                result = clsfController.classifyPost("CNB", URL);
        }
        if (!chosenClsf.equals("TF-IDF")) {
            result = clsfController.classifyPost(c, URL);
        }
        image = "images/piechart.png";
        
    }

    public List<SelectItem> toSelectItems() {
        List<SelectItem> items = new ArrayList<>();
        for (String value : clsf) {
            items.add(new SelectItem(value));
        }
        return items;
    }

    // Remove leading "www" from a URL's host if present.
    public String removeWwwFromUrl(String url) {
        int index = url.indexOf("://www.");
        if (index != -1) {
            return url.substring(0, index + 3)
                    + url.substring(index + 7);
        }
        return (url);
    }

    public String drawChart() {
        chart = new DrawPieChart();
        return chart.createGeneralChart();
    }
}
