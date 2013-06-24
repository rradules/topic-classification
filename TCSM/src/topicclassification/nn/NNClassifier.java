/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicclassification.nn;

import java.net.URL;


/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class NNClassifier {
    private String givenURL;
    private URL verifiedURL;

    public NNClassifier() {
    }

    public String getGivenURL() {
        return givenURL;
    }

    public void setGivenURL(String givenURL) {
        this.givenURL = givenURL;
    }

    public URL getVerifiedURL() {
        return verifiedURL;
    }

    public void setVerifiedURL(URL verifiedURL) {
        this.verifiedURL = verifiedURL;
    }
    
     
    
}
