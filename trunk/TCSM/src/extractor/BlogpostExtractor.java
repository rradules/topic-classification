/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import controller.MainController;
import functions.MetaTag;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class BlogpostExtractor extends AbstractExtractor {

    private final int MAX_LENGHT = 10000;

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
                        aux = normalizeContent(cont.text().toLowerCase());
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
            title = normalizeContent(doc.select("title").text());
            description = normalizeContent(new MetaTag().getMetaTag(doc, "description"));
            address = verifiedURL.toString();
            date = getPostDate(verifiedURL);

            System.out.println("Length: " + content.length());

            return MainController.getInstance().addBlogpost(address, date, title, content, description, domain);

        } catch (Exception ex) {
            Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getPostDate(URL url) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm");
        Date date = null;
        String path = url.getPath();
        Pattern p = Pattern.compile("((19|20)\\d\\d)/(0?[1-9]|1[012])");
        Matcher m = p.matcher(path);
        if (m.find()) {
            String aux = m.group();
            try {
                date = sdf.parse(aux);
            } catch (ParseException ex) {
                Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            return date;
        }
//        } else {
//            try {
//                Document doc = Jsoup.connect(url.toString()).get();
//                Elements metaElem = doc.select("div[class]");
//                for (Element meta : metaElem) {
//                    if (meta.attr("class").contains("date") || meta.attr("class").contains("meta")) {
//                        m = p.matcher(meta.text());
//                        if (m.find()) {
//                            String aux = m.group();
//                            try {
//                                date = sdf.parse(aux);
//                            } catch (ParseException ex) {
//                                Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            return date;
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(BlogpostExtractor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        return null;
    }

    public String trimToSize(String string) {
        String newString = string.trim().substring(0, 20000);
        return newString;
    }
}
