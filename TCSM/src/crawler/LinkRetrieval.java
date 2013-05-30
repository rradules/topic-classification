/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class LinkRetrieval {
    // Parse through page contents and retrieve links.

    public ArrayList retrieveLinks(Document doc, URL pageUrl, HashSet crawledList, boolean limitHost) {

        System.out.println("Crawling: " + pageUrl.toString());

        ArrayList<String> links = new ArrayList<>();

        // Document doc = Jsoup.connect(pageUrl.toString()).get();
        Elements allLinks = doc.select("a[href]");

        for (Element linkElem : allLinks) {
            // get the value from href attribute
            String link = linkElem.attr("href");
            // Skip empty links.
            if (link.length() < 1) {
                continue;
            }

            // Skip links that are just page anchors.
            if (link.charAt(0) == '#') {
                continue;
            }

            // Skip mailto links.
            if (link.indexOf("mailto:") != -1) {
                continue;
            }

            // Skip JavaScript links.
            if (link.toLowerCase().indexOf("javascript") != -1) {
                continue;
            }

            // Prefix absolute and relative URLs if necessary.


            if (link.indexOf("//") == -1) {
                // Handle absolute URLs.
                if (link.charAt(0) == '/') {
                    link = "http://" + pageUrl.getHost() + link;
                    // Handle relative URLs.
                } else {
                    String file = pageUrl.getFile();
                    if (file.indexOf('/') == -1) {
                        link = "http://" + pageUrl.getHost() + "/" + link;
                    } else {
                        String path =
                                file.substring(0, file.lastIndexOf('/') + 1);
                        link = "http://" + pageUrl.getHost() + path + link;
                    }
                }
            }

            // Remove anchors from link.
            int index = link.indexOf('#');
            if (index != -1) {
                link = link.substring(0, index);
            }

            // Remove leading "www" from URL's host if present.
            link = removeWwwFromUrl(link);

            // Verify link and skip if invalid.
            URL verifiedLink = verifyUrl(link);
            // URL pageLink = verifyUrl(linkPage);

            if (verifiedLink == null) {
                continue;
            }

            /* If specified, limit links to those
             having the same host as the start URL. */


            if (limitHost && !pageUrl.getHost().toLowerCase().equals(
                    verifiedLink.getHost().toLowerCase())) {
                continue;
            }

            // Skip link if it has already been crawled.
            if (crawledList.contains(link)) {
                continue;
            }

            // System.out.println(link);
            // Add link to list.
            links.add(link);

        }

        return links;
    }

    // Verify URL format.
    public URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://")) {
            return null;
        }

        // Verify format of URL.
        URL verifiedUrl;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        return verifiedUrl;
    }

    // Remove leading "www" from a URL's host if present.
    public String removeWwwFromUrl(String url) {
        int index = url.indexOf("://www.");
        if (index != -1) {
            return url.substring(0, index + 3)
                    + url.substring(index + 7);
        }
        return (url);
    }
}
