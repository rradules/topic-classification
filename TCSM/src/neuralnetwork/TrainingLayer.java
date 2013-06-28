/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;
import topicclassification.DocumentFrequency;
import topicclassification.TermFrequency;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.LearningTable;
import model.TempKeyword;
import topicclassification.Document;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TrainingLayer {

    private InputSetBuilder isb;
    private HashMap<String, String> final_posts;
    private DocumentFrequency docFreq;
    private TermFrequency tf;
    private NNController nnController;
    private static String[] topics = {"Activism",
        "Business and finance", "Art", "Travel",
        "Gastronomy", "Personal journal", "Literature", "Fashion",
        "Politics", "Religion and spirituality"};
    private List<LearningTable> existent;

    public TrainingLayer() {
        isb = new InputSetBuilder();
        final_posts = new HashMap<>();
        docFreq = new DocumentFrequency();
        tf = new TermFrequency();
        nnController = new NNController();
    }

    public void trainNetwork() {
        isb.buildInput();

        Document allDocs = new Document();
        allDocs.setDocuments(isb.getAllPosts());
        allDocs.setInfo("allDocs");

        docFreq.setDocument(allDocs);
        tf.setDocFreq(docFreq);

        final_posts = isb.getFinal_posts();
       // Iterator it = final_posts.keySet().iterator();

        // while (it.hasNext()) {

        //  String key = it.next().toString();
      //  for (int i = 1; i < 2; i++) {
            String key = topics[9];
            System.out.println(key);
            String content = final_posts.get(key);
            List<TempKeyword> temp = buildTempKeywords(content, key);
            applyTrainingAlgorithm(temp);

      //  }
    }

    public List<TempKeyword> buildTempKeywords(String post, String topic) {
        ArrayList<String> content = new ArrayList<>();
        content.add(post);

        Document doc = new Document();
        doc.setDocuments(content);
        doc.setInfo(topic);
        tf.setDocument(doc);
        tf.computeTFIDF();

        HashMap<String, Double> wordset = tf.getTermfrequency();
        return nnController.getOrderedKeywords(wordset, topic);

    }

    public void applyTrainingAlgorithm(List<TempKeyword> temp) {

        for (TempKeyword tk : temp) {
            //Case 1 word not in learning table - insert keyword
            if (MainController.getInstance().findLearningTableByKeyword(tk.getKeyword()).isEmpty()) {
                MainController.getInstance().addLearningTable(tk);
            } else {
                existent = MainController.getInstance().findLearningTableByKeyword(tk.getKeyword());
                for (LearningTable k : existent) {
                    //Case 2 the word is in the learning table under the same category - update weight
                    if (k.getIdCategory().getIdCategory() == tk.getIdCategory().getIdCategory()) {

                        MainController.getInstance().updateLearningTableWeight(k, tk.getWeight());
                    } else {
                        //Case 3 the word is in the learning table under another category - update weights and
                        MainController.getInstance().updateLearningTable(k, tk);
                    }
                    if (!containsCategory(existent, tk.getIdCategory().getIdCategory())) {
                        MainController.getInstance().addLearningTable(tk);
                    }

                }
            }
        }
    }

    public boolean containsCategory(List<LearningTable> words, int idCat) {
        for (LearningTable k : words) {
            if (k.getIdCategory().getIdCategory() == idCat) {
                return true;
            }
        }
        return false;

    }
}
