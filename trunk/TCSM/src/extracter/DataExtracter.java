/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extracter;

import extracter.AbstractExtracter;
import extracter.BlogpostExtracter;
import extracter.BlogrollExtracter;
import extracter.DomainExtracter;
import extracter.LocationExtracter;

/**
 *
 * @author Student
 */
public class DataExtracter {

    private String url;
    private String code;
    private AbstractExtracter extracter;

    public DataExtracter(String code, String url) {
        this.url = url;
        this.code = code;

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
