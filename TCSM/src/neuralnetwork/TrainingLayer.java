/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;
import functions.DocumentFrequency;
import functions.TermFrequency;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.Blogpost;
import model.Keyword;
import model.TempKeyword;
import topicclassification.Document;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TrainingLayer {

    private InputSetBuilder isb;
    private HashMap<String, List<Blogpost>> final_posts;
    private DocumentFrequency docFreq;
    private TermFrequency tf;
    private NNController nnController;

//    private static String[] topics = {"Activism",
//        "Business and finance", "Art", "Travel", "Photography",
//        "Gastronomy", "Personal journal", "Literature", "Fashion",
//        "Politics", "Religion and spirituality"};
    public TrainingLayer() {
        isb = new InputSetBuilder();
        final_posts = new HashMap<>();
        docFreq = new DocumentFrequency();
        tf = new TermFrequency();
        nnController = new NNController();
    }

    public void trainNetwork() {
        isb.buildInput();

//        Document allDocs = new Document();
//        allDocs.setDocuments(isb.getAllContent());
//        allDocs.setInfo("allDocs");
//
//        docFreq.setDocument(allDocs);
//        tf.setDocFreq(docFreq);

        final_posts = isb.getFinal_posts();
        Iterator it = final_posts.keySet().iterator();

        //   while (it.hasNext()) {
        String key = it.next().toString();
        List<Blogpost> posts = final_posts.get(key);
        for (Blogpost bp : posts) {
            // List<TempKeyword> temp = buildTempKeywords(bp.getBlogContent(), key);
            // applyTrainingAlgorithm(temp);
        }
        //   }
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
        boolean added = false;
        for (TempKeyword tk : temp) {
            //Case 1 word not in learning table - insert keyword
            if (MainController.getInstance().findKeywordsByKeyword(tk.getKeyword()).isEmpty()) {
                MainController.getInstance().addKeyword(tk);
            } else {
                List<Keyword> existent = MainController.getInstance().findKeywordsByKeyword(tk.getKeyword());
                for (Keyword k : existent) {
                    //Case 2 the word is in the learning table under the same category - update weight
                    if (k.getIdCategory().getIdCategory() == tk.getIdCategory().getIdCategory()) {
                        added = true;
                        MainController.getInstance().updateKeywordWeight(k, tk.getWeight());
                    }
                }
                for (Keyword k : existent) {
                    //Case 3 the word is in the learning table under another category - update weights and
                    MainController.getInstance().updateKeyword(k, tk);
                    if (!added) {
                        MainController.getInstance().addKeyword(tk);
                    }
                }
            }
        }
    }
}
