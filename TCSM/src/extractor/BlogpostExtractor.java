/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import functions.AccentElim;
import functions.MetaTag;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogpostExtractor extends AbstractExtractor {

    private final int MAX_LENGHT = 10000;
    private AccentElim accentElim;

    public BlogpostExtractor(String url) {
        super(url);
        accentElim = new AccentElim();
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
        String aux;
        StringBuilder builder = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url).get();
            URL verifiedURL = normalizeURL(url);
            domain = MainController.getInstance().findDomainByName(verifiedURL.getHost());
            Elements contentElem = doc.select("div[class]");

            for (Element cont : contentElem) {
                if ((cont.attr("class").contains("content") || cont.attr("class").contains("entry"))
                        && !cont.attr("class").contains("comment") && !cont.attr("class").contains("widget")) {
                    if (!cont.text().equals("")) {
                        // aux = elliminateAccents(cont.text().toLowerCase());
                        aux = cont.text().toLowerCase();

                        if (!contentList.contains(aux)) {
                            contentList.add(aux);
                            builder.append(aux);
                            //System.out.println(aux);
                        }
                    }
                }

            }
            String auxContent = builder.toString();
            if (builder.toString().length() > 20000) {
                auxContent = trimToSize(builder.toString());
            }
            content = auxContent;
            title = accentElim.elliminateAccents(doc.select("title").text());
            description = accentElim.elliminateAccents(new MetaTag().getMetaTag(doc, "description"));
            address = verifiedURL.toString();
            date = getPostDate(verifiedURL);

            //System.out.println("Length: " + content.length());
            //System.out.println("Address: "+ address);
            if (domain != null) {
                return MainController.getInstance().addBlogpost(address, date, title, content, description, domain);
            }

        } catch (Exception ex) {
            Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getPostDate(URL url) {
        Date date;
        String path = url.getPath();
        Pattern p1 = Pattern.compile("((19|20)\\d\\d)/(0?[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])");
        Pattern p2 = Pattern.compile("((19|20)\\d\\d)/(0?[1-9]|1[012])");
        Matcher m1 = p1.matcher(path);
        Matcher m2 = p2.matcher(path);
        if (m1.find()) {
            //System.out.println(m1.group());
            int year = Integer.parseInt(m1.group(1));
            int month = Integer.parseInt(m1.group(3));
            int day = Integer.parseInt(m1.group(4));
            //System.out.println(year + " " + month + " " + day);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, day, 0, 0, 0);
            date = cal.getTime();

            return date;

        } else {
            if (m2.find()) {
                int year = Integer.parseInt(m2.group(1));
                int month = Integer.parseInt(m2.group(3));
                //System.out.println(year + " " + month);
                Calendar cal = Calendar.getInstance();
                cal.set(year, month - 1, 1, 0, 0, 0);
                date = cal.getTime();

                return date;
            }
        }
        return null;
    }

    public String trimToSize(String string) {
        String newString = string.trim().substring(0, 20000);
        return newString;
    }
}
