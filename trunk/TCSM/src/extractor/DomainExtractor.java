/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import functions.MetaTag;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Student
 */
public class DomainExtractor extends AbstractExtractor {

    MetaTag metaTag = new MetaTag();

    public DomainExtractor(String url) {
        super(url);
    }

    //    public Domain addDomain(String name, Location loc, String robots, 
    // int depth, Date activation)
    @Override
    public Object getData() {
        try {
            Document doc = Jsoup.connect(url).get();
            
            URL verifiedURL = new URL(url);
          
            String description = metaTag.getMetaTag(doc, "description");
            String robots = metaTag.getMetaTag(doc, "robots");
            String name = verifiedURL.getHost();
            Location loc = (Location) new LocationExtractor(url).getData();
            
            
        } catch (IOException ex) {
            Logger.getLogger(DomainExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
