/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Category;
import topicclassification.Document;
import topicclassification.DocumentConstructor;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class InputSetBuilder {

    private List<Category> categories;
    private HashMap<String, String> final_posts;
    private ArrayList<String> allPosts;
    DocumentConstructor docCons;
    private final int MAX_SIZE = 10;

    public InputSetBuilder() {
        final_posts = new HashMap<>();
        allPosts = new ArrayList<>();
        docCons = new DocumentConstructor();
    }

    public HashMap<String, String> getFinal_posts() {
        return final_posts;
    }

    public ArrayList<String> getAllPosts() {
        return allPosts;
    }

    public int getDocNo() {
        return allPosts.size();
    }

    public void buildInput() {
        categories = MainController.getInstance().getAllCategories();
        for (Category cat : categories) {
            Document doc = docCons.getXDocuments(cat.getCategory(), MAX_SIZE);
            allPosts.addAll(doc.getParsedDocuments());
            final_posts.put(cat.getCategory(), doc.getParsedContent());
        }

    }
}
