/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Student
 */
public class DomainExtractor extends AbstractExtractor {

    public DomainExtractor(String url) {
        super(url);
    }

    //    public Domain addDomain(String name, String suffix, String robots, 
    // int depth, Date activation)
    @Override
    public Object getData() {

        throw new UnsupportedOperationException("Not supported yet.");
    }


}
