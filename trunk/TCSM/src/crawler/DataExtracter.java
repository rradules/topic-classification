/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Student
 */
public class DataExtracter {

    private String url;
    private String code;
    private Document doc;
    private AbstractExtracter extracter;

    public DataExtracter(String code, String url) {
        this.url = url;
        this.code = code;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(DataExtracter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object extractData() {
        switch (code) {
            case "location": {
                extracter = new LocationExtracter(url);
                break;
            }
            case "domain": {
                extracter = new DomainExtracter(url);
                break;
            }
            case "blogroll": {
                extracter = new BlogrollExtracter(url);
                break;
            }
            case "blogpost": {
                extracter = new BlogpostExtracter(url);
                break;
            }
            default:
                return null;
        }
        return extracter.getData();
    }
}
