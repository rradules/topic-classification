/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Domain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Student
 */
public class DataExtracter {

    URL url;
    Document doc;

    public DataExtracter(URL url) {
        this.url = url;
        try {
            doc = Jsoup.connect(url.toString()).get();
        } catch (IOException ex) {
            Logger.getLogger(DataExtracter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Domain extractDomain() {
        return null;
    }

    public void extractBlogroll() {
    }

    public void extractBlogpost() {
    }
}
