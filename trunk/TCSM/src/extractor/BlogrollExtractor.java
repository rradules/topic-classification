/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Blogroll;
import model.Domain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogrollExtractor extends AbstractExtractor {

    public final int WORDPRESS = 0;

    public BlogrollExtractor(String url) {
        super(url);
    }

    @Override
    public Object getData() {
        ArrayList<Blogroll> blogrollList = new ArrayList<>();
        Domain domain, destination;
        int type, idDest;
        try {
            Document doc = Jsoup.connect(url).get();
            URL verifiedURL = normalizeURL(url);
            //= Crawler.getInstance().getCurrentDomain();         
            if (url.contains("wordpress")) {
                type = WORDPRESS;
            } else {
                type = 1;
            }
            domain = MainController.getInstance().findDomainByName(verifiedURL.getHost());

            Elements blogrollLinks = doc.select("ul[class=xoxo blogroll] li a[href]");

            for (Element link : blogrollLinks) {
                if (link != null) {
                    String blog = link.attr("href");
                    URL dest = normalizeURL(blog);
                    destination = MainController.getInstance().findDomainByName(dest.getHost());
                    idDest = -1;
                    if (destination != null) {
                        idDest = destination.getIdDomain();
                    }
                 //   blogrollList.add(MainController.getInstance().addBlogroll(domain, dest.toString(), type, idDest));
                }
            }
            return blogrollList;
        } catch (Exception ex) {
            Logger.getLogger(BlogrollExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        // throw new UnsupportedOperationException("Not supported yet.");
    }

}
