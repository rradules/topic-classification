/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import crawler.Crawler;

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

        Crawler crawler = new Crawler();
        crawler.search("http://www.ciutacu.ro/", 10);

        }

}
