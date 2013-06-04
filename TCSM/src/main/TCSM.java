/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import crawler.Crawler;
import extractor.BlogpostExtractor;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TCSM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Crawler.getInstance().search("http://nastase.wordpress.com/", 10);
        BlogpostExtractor be = new BlogpostExtractor("http://tudorchirila.blogspot.ro/2013/05/junk-thoughts.html");
        be.getData();


    }
}
