/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

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
}
