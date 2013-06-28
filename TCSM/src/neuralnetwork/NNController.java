/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.TempKeyword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class NNController {

    private final int MAX_WORDS = 200;

    public NNController() {
    }

    public List<TempKeyword> getOrderedKeywords(HashMap<String, Double> wordset, String category) {
        Iterator it = wordset.keySet().iterator();
        MainController.getInstance().emptyTempTable();

        while (it.hasNext()) {
            String word = it.next().toString();
            double weight = wordset.get(word);
            if (weight > 2) {
                MainController.getInstance().addTempKeyword(word, weight, category);
            }
        }

        List<TempKeyword> tempKeywords = MainController.getInstance().getXOrderedTempKeywordb(MAX_WORDS);
//        System.out.println("Category: " + category);
//        for (TempKeyword tk : tempKeywords) {
//            System.out.println("Keyword: " + tk.getKeyword() + " - "+tk.getWeight());
//        }
        return tempKeywords;
    }
}
