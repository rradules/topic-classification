/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import extractor.DataExtractor;
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
    //limit links to host
    private boolean limitToHost = true;
    //log files
    private String logFile = "crawlerLog.txt";
    //robots.txt parser
    private RobotsParser roboParser;
    //link retrieval module
    private LinkRetrieval linkRetrieval;
    //current domain crawling
    private Domain currentDomain;
    private static Crawler singleton = null;
    private DataExtractor dataExtractor = null;

    private Crawler() {
        roboParser = new RobotsParser();
        linkRetrieval = new LinkRetrieval();
    }

    public static Crawler getInstance() {
        if (singleton == null) {
            singleton = new Crawler();
        }
        return singleton;
    }

    public boolean isCrawling() {
        return crawling;
    }

    public Domain getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(Domain currentDomain) {
        this.currentDomain = currentDomain;
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
            @Override
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

                    //add domain in DB and fix current domain
                    dataExtractor = new DataExtractor("domain", verifiedUrl.toString());
                    currentDomain = (Domain) dataExtractor.extractData();
                    //extract the blogroll
                    dataExtractor = new DataExtractor("blogroll", verifiedUrl.toString());
                    System.out.println("Blogroll: " + dataExtractor.extractData());

                    // Turn crawling flag on.
                    crawling = true;
                    // Perform the actual crawling.
                    crawl(verifiedUrl.toString(), maxUrls, limitToHost);
                    // Turn crawling flag off.
                    crawling = false;
                    // Close matches log file.
                    try {
                        logFileWriter.close();

                    } catch (Exception e) {
                        showError("Unable to close matches log file.");
                    }

                } catch (Exception ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
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

            //extract the Blogpost for each crawled page except the starting one
            if (!verifiedUrl.toString().equals(startUrl)) {
                dataExtractor = new DataExtractor("blogpost", verifiedUrl.toString());
                System.out.println("Blogpost: " + dataExtractor.extractData());
            }

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
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
}
