/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.craigslist;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author seh
 */
public class ScrapeCraigslist {
    
    
    public static void main(String[] args) throws IOException {
        Map<String,String> cityURL = new HashMap();
        Map<String,String> catURL = new HashMap();
        
        scrape("http://geo.craigslist.org/iso/us", cityURL, null);
        scrape("http://newyork.craigslist.org/", cityURL, catURL);
        scrape("http://sfbay.craigslist.org/", cityURL, catURL);
        
        System.out.println(cityURL);
        System.out.println(catURL);
        
        Craigslist cl = new Craigslist(cityURL, catURL);
        cl.save();
    }

    private static void scrape(String u, Map<String,String> cities, Map<String,String> categories) throws IOException {
        //http://geo.craigslist.org/iso/us
        
        Document d = Jsoup.parse(new URL(u), 1000);
        Elements tags = d.getElementsByTag("a");
        Iterator<Element> ie = tags.iterator();
        while (ie.hasNext()) {
            Element e = ie.next();
            String link = e.attr("href");
            String text = e.text();
            if (cities!=null) {
                if (!text.equals("more ...")) {
                    if (link.contains(".craigslist.org")) {
                        if ((!link.contains("www.craigslist.org")) && (!link.contains("blog.craigslist.org"))) {
                            if (link.startsWith("http://")) {                        
                                //System.out.println(text + " " + link);
                                link = removeTrailingSlash(link);
                                cities.put(text, link);
                            }
                        }
                    }
                }
            }
            
            if (categories!=null) {
                if ((!link.startsWith("http://")) && (!link.startsWith("https://"))) {
                    if ((!link.startsWith("/forums")) && (!link.startsWith("/cgi-bin")) && (!link.startsWith("cal/")) && (!link.contains("?"))) {
                        //System.out.println(text + " " + link);
                        link = removeTrailingSlash(link);
                        link = removeLeadingSlash(link);
                        categories.put(text, link);                    
                    }
                }
            }
        }
        
        
    }

    private static String removeTrailingSlash(String link) {
        if (link.endsWith("/"))
            link = link.substring(0, link.length()-1);
        return link;
    }
    private static String removeLeadingSlash(String link) {
        if (link.startsWith("/"))
            link = link.substring(1, link.length());
        return link;
    }
    
}
