/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import crawler.LinkRetrieval;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student
 */
public abstract class AbstractExtractor {

    String url;

    public AbstractExtractor(String url) {
        this.url = url;
    }

    public abstract Object getData();

    public URL normalizeURL(String url) {
        try {
            String newUrl = new LinkRetrieval().removeWwwFromUrl(url);
            URL verifiedURL = new URL(newUrl);

            return verifiedURL;
        } catch (MalformedURLException ex) {
            Logger.getLogger(AbstractExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
