/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import functions.MetaTag;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Domain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Student
 */
public class BlogpostExtractor extends AbstractExtractor {

    public BlogpostExtractor(String url) {
        super(url);
    }
//public Blogpost addBlogpost(String address, Date date,
//            String title, String content,
//            String description, Domain domain) throws Exception {

    @Override
    public Object getData() {
        ArrayList<String> contentList = new ArrayList<>();
        String title, content, address, description;
        Domain domain;
        Date date;
        StringBuilder builder = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url).get();
            URL verifiedURL = normalizeURL(url);
            domain = MainController.getInstance().findDomainByName(verifiedURL.getHost());
            Elements contentElem = doc.select("div[class]");
            for (Element cont : contentElem) {
                if (cont.attr("class").contains("content") && !cont.attr("class").contains("comment")) {
                    if (!cont.text().equals("")) {
                        if (!contentList.contains(cont.text())) {
                            contentList.add(cont.text());
                            System.out.println(cont.text());
                            builder.append(cont.text());
                        }
                    }
                }
            }
            content = builder.toString();
            title = doc.select("title").text();
            description = new MetaTag().getMetaTag(doc, "description");
            address = verifiedURL.toString();
            // return MainController.getInstance().addBlogpost(address, date, title, content, description, domain);


        } catch (IOException ex) {
            Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean containsDate(URL url) {
        if (url.getHost().contains("blogspot") || url.getHost().contains("wordpress")) {
            String path = url.getPath();

            Pattern p = Pattern.compile("((19|20)\\d\\d)/(0?[1-9]|1[012])");
            Matcher m = p.matcher(path);
            if (m.find()) {
                return true;
            }
        }
        return true;
    }
}
