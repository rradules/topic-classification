/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.MainController;
import java.util.ArrayList;
import java.util.List;
import model.Blogpost;
import model.Domain;
import topicclassification.TFIDFClassifier;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestClassifiers {
//"Activism", "Business and finance", "Art", "Travel",
    //  "Gastronomy", "Literature", "Fashion", 
    //"Politics", "Religion and spirituality"

    private static final String[] topics = {"Activism"};

    public static void main(String[] args) {


        for (String s : topics) {
            System.out.println(s);
            // ArrayList<Integer> domains = new ArrayList<>();
            int countplus = 0;
            int countminus = 0;
            //ArrayList<Blogpost> posts = MainController.getInstance().getProcessesBlogpostsByCateg(0, s);
            List<Domain> domCat = MainController.getInstance().findDomainByCategory(s);
            System.out.println(domCat.size());
            
            for (Domain dm : domCat) {
               // System.out.println(dm.getDomainName());
                TFIDFClassifier classifier = new TFIDFClassifier();
                String dim = classifier.classifyBlog(dm);
                if (dim.equals(dm.getIdCategory().getCategory())) {
                    countplus++;
                } else {
                    System.out.println("Missclass: " + dm.getDomainName() + " to " + dim);
                    countminus++;
                }
            }

//            for (Blogpost bp : posts) {
//                int domain = bp.getIdDomain().getIdDomain();
//                if (domains.contains(domain)) {
//                    continue;
//                } else {
//                    domains.add(domain);
//                    TFIDFClassifier classifier = new TFIDFClassifier();
//                    String dim = classifier.classifyBlog(bp.getIdDomain());
//                    if (dim.equals(bp.getIdDomain().getIdCategory().getCategory())) {
//                        countplus++;
//                    } else {
//                        System.out.println("Missclass: " + bp.getIdDomain().getDomainName() + " to " + dim);
//                        countminus++;
//                    }
//                }
//            }

            System.out.println("+ " + countplus);
            System.out.println("- " + countminus);

        }
    }
}
