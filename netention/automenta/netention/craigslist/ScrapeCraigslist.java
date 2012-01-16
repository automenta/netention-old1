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
        
        scrapeCities("http://geo.craigslist.org/iso/us", cityURL, null);
        scrapeCities("http://newyork.craigslist.org/", cityURL, catURL);
        scrapeCities("http://sfbay.craigslist.org/", cityURL, catURL);
        
        System.out.println(cityURL);
        System.out.println(catURL);
        
        
    }

    private static void scrapeCities(String u, Map<String,String> cities, Map<String,String> categories) throws IOException {
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
                                cities.put(text, link);
                            }
                        }
                    }
                }
            }
            
            if (categories!=null) {
                if ((!link.startsWith("http://")) && (!link.startsWith("https://"))) {
                    if ((!link.startsWith("/forums")) && (!link.startsWith("/cgi-bin")) && (!link.startsWith("cal/"))) {
                        //System.out.println(text + " " + link);
                        categories.put(text, link);                    
                    }
                }
            }
        }
        
        
        
        
        
    }

    private static void scrapeCategories() {
    }
    
}
