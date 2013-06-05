/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class MetaTag {

    public MetaTag() {
    }

    public String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) {
                return s;
            }
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) {
                return s;
            }
        }
        return null;
    }
}
