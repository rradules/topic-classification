/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import crawler.CrawlerInfo;

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

        System.out.println("Crawl");

        CrawlerInfo crawler = new CrawlerInfo();
        crawler.search("test.txt", "http://eusimuntele.blogspot.ro/", 10);

    }
}
