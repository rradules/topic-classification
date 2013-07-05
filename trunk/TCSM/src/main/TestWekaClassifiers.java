
import controller.MainController;
import java.util.ArrayList;
import model.Blogpost;
import topicclassification.ClassifierFactory;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestWekaClassifiers {

//"Activism", "Business and finance", "Art", "Travel",
    //  "Gastronomy", "Literature", "Fashion", 
    //"Politics", "Religion and spirituality"
    private static final String[] topics = {//"Activism", "Business and finance", "Art", 
        "Travel",
       // "Gastronomy", "Literature", "Fashion",
       // "Politics", "Religion and spirituality"
    };

    public static void main(String[] args) {

        for (String s : topics) {
            System.out.println(s);
            ArrayList<Integer> domains = new ArrayList<>();
            int countplus = 0;
            int countminus = 0;
            ArrayList<Blogpost> posts = MainController.getInstance().getProcessesBlogpostsByCateg(1, s);

            for (Blogpost bp : posts) {
                int domain = bp.getIdDomain().getIdDomain();
                if (domains.contains(domain)) {
                    continue;
                } else {
                    domains.add(domain);
                    ClassifierFactory classFactory = new ClassifierFactory("SMO");
                    String dim = classFactory.getTopic(bp.getIdDomain().getDomainName());
                    if (dim.equals(bp.getIdDomain().getIdCategory().getCategory())) {
                        countplus++;
                    } else {
                        System.out.println("Missclass: " + bp.getIdDomain().getDomainName() + " to " + dim);
                        countminus++;
                    }
                }
            }

            System.out.println("+ " + countplus);
            System.out.println("- " + countminus);

        }
    }
}
