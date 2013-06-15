/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import crawler.LinkRetrieval;
import functions.MetaTag;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class DomainExtractor extends AbstractExtractor {

    MetaTag metaTag = new MetaTag();

    public DomainExtractor(String url) {
        super(url);
    }

    //    public Domain addDomain(String name, Location loc, String robots, 
    // String description, Date activation)
    @Override
    public Object getData() {
        try {
            Document doc = Jsoup.connect(url).get();

            url = new LinkRetrieval().removeWwwFromUrl(url);

            URL verifiedURL = new URL(url);

            String description = metaTag.getMetaTag(doc, "description");
            String robots = metaTag.getMetaTag(doc, "robots");
            String name = verifiedURL.getHost();
            Location loc = (Location) new LocationExtractor(url).getData();
            Date activation = getActivationDate(doc);

            return MainController.getInstance().addDomain(name, loc, robots, description, activation);


        } catch (Exception ex) {
            Logger.getLogger(DomainExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Date getActivationDate(Document doc) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = null;
        try {
            Element lastDiv = doc.select("div[class=footer]").last();
            Pattern p = Pattern.compile("(19|20)\\d\\d");
            if (lastDiv != null) {
                Matcher m = p.matcher(lastDiv.text());

                if (m.find()) {
                    String year = m.group();
                    date = sdf.parse(year);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(DomainExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
}
