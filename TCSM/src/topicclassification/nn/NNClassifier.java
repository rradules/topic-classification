/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import controller.MainController;
import functions.ScoreCalculator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Domain;
import topicclassification.ml.Document;
import topicclassification.ml.DocumentConstructor;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class NNClassifier {

    private String givenURL;
    private URL verifiedURL;
    private DocumentConstructor docConstructor;
    private ScoreCalculator scoreCalc;

    public NNClassifier() {
        docConstructor = new DocumentConstructor();
        scoreCalc = new ScoreCalculator();
    }

    public String getGivenURL() {
        return givenURL;
    }

    public void setGivenURL(String givenURL) {
        this.givenURL = givenURL;
    }

    public void classifyBlog(String url) {
        givenURL = url;
        if (!url.equals("")) {
            try {

                verifiedURL = new URL(url);
                String domName = verifiedURL.getHost();
                Domain domain = MainController.getInstance().findDomainByName(domName);
                if (domain == null) {
                    System.out.println("Adding new domain in the DB");
                    //Crawler.getInstance().search(verifiedURL.toString(), 6);
                    //domain = MainController.getInstance().findDomainByName(domName);
                }

                Document document = docConstructor.getDocumentToClassify(domName);
                String content = document.getParsedContent();
                String[] tokens = content.split("\\s+");
                System.out.println("Tokens: " + tokens.length);
                HashMap<Integer, Double> scores = scoreCalc.getScore(tokens);
                printScores(scores);

            } catch (MalformedURLException ex) {
                Logger.getLogger(NNClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void printScores(HashMap<Integer, Double> scores) {
        Iterator it = scores.keySet().iterator();

        while (it.hasNext()) {
            int categ = (Integer) it.next();
            double val = scores.get(categ);
            String categName = MainController.getInstance().findCategoryById(categ).getCategory();

            System.out.println(categName + " - " + val);
        }
    }
}
