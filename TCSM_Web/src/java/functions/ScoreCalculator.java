/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import controller.MainController;
import java.util.HashMap;
import java.util.List;
import model.Keyword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ScoreCalculator {

    private HashMap<Integer, Double> score;

    public ScoreCalculator() {
        score = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            score.put(i, 0.0);
        }
    }

    public HashMap<Integer, Double> getScore(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            List<Keyword> keys = MainController.getInstance().findKeywordsByKeyword(tokens[i]);
            for (Keyword k : keys) {
                int idCateg = k.getIdCategory().getIdCategory();
                double weight = score.get(idCateg) + k.getWeight();
                score.put(idCateg, weight);
            }
        }
        return score;
    }
}
