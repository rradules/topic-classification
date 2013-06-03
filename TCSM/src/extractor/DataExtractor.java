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
    private AbstractExtractor extractor;

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
                extractor = new LocationExtractor(url);
                break;
            }
            case "domain": {
                extractor = new DomainExtractor(url);
                break;
            }
            case "blogroll": {
                extractor = new BlogrollExtractor(url);
                break;
            }
            case "blogpost": {
                extractor = new BlogpostExtractor(url);
                break;
            }
            default:
                return null;
        }
        return extractor.getData();
    }
}
