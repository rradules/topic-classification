/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import crawler.Crawler;
import crawler.LinkRetrieval;
import java.io.IOException;
import java.net.MalformedURLException;
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
        try {
            Document doc = Jsoup.connect(url).get();
            URL verifiedURL = normalizeURL(url);
            Domain domain;
            //= Crawler.getInstance().getCurrentDomain();
            int type;
            if (url.contains("wordpress")) {
                type = WORDPRESS;
            } else {
                type = 1;
            }
            domain = MainController.getInstance().findDomainByName(verifiedURL.getHost());

            Elements blogrollLinks = doc.select("ul[class=xoxo blogroll] li a[href]");
            System.out.println(blogrollLinks.size());
            for (Element link : blogrollLinks) {
                if (link != null) {
                    String blog = link.attr("href");
                    URL dest = normalizeURL(blog);
                    Domain destination = MainController.getInstance().findDomainByName(dest.getHost());
                    int idDest = -1;
                    if (destination != null) {
                        idDest = destination.getIdDomain();
                    }
                    blogrollList.add(MainController.getInstance().addBlogroll(domain, dest.toString(), type, idDest));
                }
            }
            return blogrollList;
        } catch (Exception ex) {
            Logger.getLogger(BlogrollExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    public URL normalizeURL(String url) {
        try {
            String newUrl = new LinkRetrieval().removeWwwFromUrl(url);
            URL verifiedURL = new URL(newUrl);

            return verifiedURL;
        } catch (MalformedURLException ex) {
            Logger.getLogger(BlogrollExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
