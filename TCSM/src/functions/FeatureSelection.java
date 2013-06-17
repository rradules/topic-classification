/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import controller.MainController;
import java.util.Arrays;
import stemmer.RomanianStemmer;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class FeatureSelection {

    private RomanianStemmer stemmer;

    public FeatureSelection() {
        stemmer = new RomanianStemmer();
    }

    public String performFeatureSelection(String content) {
        String[] tokens = content.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String w : Arrays.asList(tokens)) {
            //remove punctuation
            String str = w.replaceAll("[^\\p{L}]", "");
            //parse only if the word is not in the stopwords list
            if (MainController.getInstance().findStopwordByStopword(str) == null) {
                stemmer.setCurrent(str);
                //stem the word
                stemmer.stem();
                //recontruct the content
                builder.append(stemmer.getCurrent()).append(" ");
            }
        }
        return builder.toString().trim();
    }
}
