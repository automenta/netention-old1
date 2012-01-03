/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.craigslist;

import automenta.netention.NMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.horrabin.horrorss.RssChannelBean;
import org.horrabin.horrorss.RssImageBean;
import org.horrabin.horrorss.RssItemBean;
import org.horrabin.horrorss.RssParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides the latest Craigslist messages
 * @author seh
 */
public class CraigslistChannel {
    private final String url;
    
    //ex: http://pittsburgh.craigslist.org/eng/index.rss

    public CraigslistChannel(String city, String category) {
        this("http://" + city + ".craigslist.org/" + category + "/index.rss");
    }
    
    public CraigslistChannel(String url) {
        super();
        this.url = url;
    }
    
    public synchronized List<NMessage> update() {
        RssParser rss = new RssParser(url);
        List<NMessage> m = new LinkedList();
        
        try{
           rss.parse();
           RssChannelBean channel = rss.getChannel(); //Obtain the channel element
           RssImageBean image = rss.getImage(); //Obtain the image element
           Vector items = rss.getItems(); //Obtain a Vector of item elements (RssItemBean)

           final DateFormat f = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss", Locale.ENGLISH);

           
           // Iterate the list items
           for (int i=0; i<items.size(); i++){
                  RssItemBean item = (RssItemBean)items.elementAt(i); //Cast the Object from the list to RssItemBean
//                  System.out.println("Title: " + item.getTitle());
//                  System.out.println("Link : " + item.getLink());
//                  System.out.println("Desc.: " + item.getDescription());
                  
                  Date date = f.parse(item.getPubDate());
                  
                  NMessage n = new NMessage(item.getLink(), item.getAuthor(), url, date, item.getTitle(), item.getDescription(), item.getCategory());
                  m.add(n);
                  
                  Document doc = Jsoup.connect(item.getLink()).get();
                  Elements es = doc.select("a");
                  Iterator<Element> ii = es.iterator();
                  while (ii.hasNext()) {
                      Element e = ii.next();
                      if (e.hasAttr("href")) {
                          String h = e.attr("href");
                          if (h.startsWith("mailto:")) {
                              n.setFrom(h);
                              break;
                          }
                      }
                  }
            }


        }catch(Exception e){
            e.printStackTrace();;
        }
        
        return m;
    }


    public static void main(String[] args) {
        
        List<NMessage> l = new CraigslistChannel("pittsburgh", "eng").update();
        for (NMessage m : l)
            System.out.println(m);
    }
    
}
