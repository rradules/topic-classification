/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

/**
 *
 * @author Student
 */
public class DataExtractor {

    private String url;
    private String code;
    private AbstractExtractor extracter;

    public DataExtractor(String code, String url) {
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
                extracter = new LocationExtractor(url);
                break;
            }
            case "domain": {
                extracter = new DomainExtractor(url);
                break;
            }
            case "blogroll": {
                extracter = new BlogrollExtractor(url);
                break;
            }
            case "blogpost": {
                extracter = new BlogpostExtractor(url);
                break;
            }
            default:
                return null;
        }
        return extracter.getData();
    }
}
