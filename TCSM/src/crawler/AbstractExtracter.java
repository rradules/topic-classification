/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author Student
 */
public abstract class AbstractExtracter {

    String url;

    public AbstractExtracter(String url) {
        this.url = url;
    }

    public abstract Object getData();
}
