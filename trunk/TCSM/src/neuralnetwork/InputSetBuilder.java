/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.Blogpost;
import model.Category;
import model.Domain;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class InputSetBuilder {

    private static int counter = 0;
    private List<Category> categories;
    private List<Domain> domains;
    private List<Blogpost> posts;
    private HashMap<String, List<Blogpost>> final_posts;
    private ArrayList<Blogpost> allposts;
    private final int MAX_SIZE = 15;

    public InputSetBuilder() {
        final_posts = new HashMap<>();
        allposts = new ArrayList<>();
    }

    public HashMap<String, List<Blogpost>> getFinal_posts() {
        return final_posts;
    }

    public int getDocNo() {
        return allposts.size();
    }

    public ArrayList<Blogpost> getAllposts() {
        return allposts;
    }

    public ArrayList<String> getAllContent() {
        ArrayList<String> content = new ArrayList<>();
        for (Blogpost bp : allposts) {
            content.add(bp.getBlogContent());
        }
        return content;
    }

    public void buildInput() {
        StringBuilder builder = new StringBuilder();
        categories = MainController.getInstance().getAllCategories();
        ArrayList<Blogpost> temp = new ArrayList<>();
        for (Category cat : categories) {
            //System.out.println("Category: " + cat.getCategory());
            temp.clear();
            counter = 0;
            domains = MainController.getInstance().findDomainByCategory(cat);
            for (Domain dom : domains) {
                //System.out.println("Domain: " + dom.getDomainName());
                if (counter > MAX_SIZE) {
                    continue;
                }
                posts = MainController.getInstance().findBlogpostByDomain(dom);
                if (posts.size() > 3) {
                    for (Blogpost pb : posts) {
                        // System.out.println(pb.getPageAddress());
                        builder.append(pb.getBlogContent());
                    }
                    allposts.addAll(posts);
                    temp.addAll(posts);
                    counter++;
                }
            }
            final_posts.put(cat.getCategory(), temp);
        }

        System.out.println(builder.toString().length());
    }

    public ArrayList<String> getContentforTopic(String topic) {
        ArrayList<String> content = new ArrayList<>();
        content.addAll((ArrayList) final_posts.get(topic));
        return content;
    }
}
