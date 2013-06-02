/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.net.URL;

/**
 *
 * @author Student
 */
public class DataExtracter {

    URL url;

    public DataExtracter() {
    }

    public DataExtracter(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
