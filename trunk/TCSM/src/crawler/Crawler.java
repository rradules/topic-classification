/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Domain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class Crawler {

    // crawling flag
    private boolean crawling = false;
    // Matches log file print writer.
    private PrintWriter logFileWriter;
    private boolean limitToHost = true;
    private String logFile = "crawlerLog.txt";
    private RobotsParser roboParser;
    private LinkRetrieval linkRetrieval;
    private Domain currentDomain;

    public Crawler() {
        roboParser = new RobotsParser();
        linkRetrieval = new LinkRetrieval();
    }

    public boolean isCrawling() {
        return crawling;
    }

    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }

    public boolean isLimitToHost() {
        return limitToHost;
    }

    public void setLimitToHost(boolean limitToHost) {
        this.limitToHost = limitToHost;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void search(final String startUrl, final int maxUrls) {
        // Start the search in a new thread.
        Thread thread = new Thread(new Runnable() {
            public void run() {

                // Open matches log file.
                try {
                    logFileWriter = new PrintWriter(new FileWriter(logFile));
                } catch (Exception e) {
                    showError("Unable to open matches log file.");
                    return;
                }
                try {
                    String url = linkRetrieval.removeWwwFromUrl(startUrl);
                    // Convert string url to URL object.
                    URL verifiedUrl = linkRetrieval.verifyUrl(url);
//                    //add domain in DB and fix current domain
                    //currentDomain = 
//                    MainController.getInstance().addLocation(verifiedUrl.getHost());
                } catch (Exception ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Turn crawling flag on.
                crawling = true;
                // Perform the actual crawling.
                crawl(startUrl, maxUrls, limitToHost);
                // Turn crawling flag off.
                crawling = false;
                // Close matches log file.
                try {
                    logFileWriter.close();
                } catch (Exception e) {
                    showError("Unable to close matches log file.");
                }
            }
        });
        thread.start();
    }

    // Show dialog box with error message.
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Perform the actual crawling, searching for the search string.
    public void crawl(String startUrl, int maxUrls, boolean limitHost) {
        // Setup crawl lists.
        HashSet crawledList = new HashSet();
        LinkedHashSet toCrawlList = new LinkedHashSet();

        // Add start URL to the to crawl list.
        toCrawlList.add(startUrl);

        /* Perform actual crawling by looping through the to crawl list. */
        while (crawling && toCrawlList.size() > 0) {
            /* Check to see if the max URL count has
             been reached, if it was specified.*/
            //  System.out.println("To Crawl list size: "+toCrawlList.size());
            //  System.out.println("Crawled list size: "+crawledList.size());

            if (maxUrls != -1) {
                if (crawledList.size() == maxUrls) {
                    break;
                }
            }

            // Get URL at bottom of the list.
            String url = (String) toCrawlList.iterator().next();

            // Remove URL from the to crawl list.
            toCrawlList.remove(url);
            url = linkRetrieval.removeWwwFromUrl(url);

            // Convert string url to URL object.
            URL verifiedUrl = linkRetrieval.verifyUrl(url);

            // Skip URL if robots are not allowed to access it.
            if (!roboParser.isRobotAllowed(verifiedUrl)) {
                continue;
            }
            // Add page to the crawled list.
            crawledList.add(url);

            // Download the page at the given url.
            Document doc;

            /* If the page was downloaded successfully, retrieve all of its
             links and then see if it contains the search string. */
            try {
                doc = Jsoup.connect(verifiedUrl.toString()).get();
//                System.out.println("Authority: "+verifiedUrl.getAuthority());
//                System.out.println("Host: "+verifiedUrl.getHost());
//                System.out.println("Path: "+verifiedUrl.getPath());
//                System.out.println("Protocol: "+verifiedUrl.getProtocol());
//                System.out.println("Query: "+verifiedUrl.getQuery());
//                System.out.println("Ref: "+verifiedUrl.getRef());
//                System.out.println("UserInfo: "+verifiedUrl.getUserInfo());
//                System.out.println("Hash code: "+verifiedUrl.hashCode());


                // Add verified URL to log file.
                try {
                    logFileWriter.println(verifiedUrl);
                } catch (Exception e) {
                    showError("Unable to log match.");
                }
                //add new domain to DB

                // Retrieve list of valid links from page.
                ArrayList<String> links = linkRetrieval.retrieveLinks(doc, verifiedUrl, crawledList, limitHost);

                // Add links to the to crawl list.
                toCrawlList.addAll(links);

            } catch (IOException ex) {
                //   Logger.getLogger(CrawlerInfo.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
}
