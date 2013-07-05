/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import topicclassification.ClassifierController;
import topicclassification.ClassifierFactory;
import topicclassification.TFIDFClassifier;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@ManagedBean
public class ClassifierBean implements java.io.Serializable {

    private String URL;
    private List<String> classifiers = new ArrayList<>();
    private String[] clsf = {"TF-IDF", "Naive Bayes", "Naive Bayes Updatable",
        "Complement Naive Bayes", "K Nearest Neighbor", "Decision tree C4.5",
        "SVM with Sequential Minimal Optimization", "MultiLayer Perceptron"};
    private String chosenClsf;
    private TFIDFClassifier myClassifier = new TFIDFClassifier();
    private ClassifierController clsfController = new ClassifierController();

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

    public String classifyBlog() {
        String c;
        switch (chosenClsf) {
            case "TF-IDF":
                return myClassifier.classifyBlog(URL);
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
                return clsfController.classifyPost("CNB", URL);
        }
        return clsfController.classifyPost(c, URL);
    }

}
