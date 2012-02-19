/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.rss;

import automenta.netention.Channel;
import automenta.netention.NMessage;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.horrabin.horrorss.RssChannelBean;
import org.horrabin.horrorss.RssImageBean;
import org.horrabin.horrorss.RssItemBean;
import org.horrabin.horrorss.RssParser;

/**
 *
 * @author seh
 */
public class RSSChannel implements Channel {
    public final String url;
 
    public RSSChannel(String url) {
        super();
        this.url = url;
    }
    
    @Override
    public synchronized List<NMessage> getMessages() {
        RssParser rss = new RssParser(url);
        List<NMessage> m = new LinkedList();
        
        try{
           rss.parse();
           RssChannelBean channel = rss.getChannel(); //Obtain the channel element
           RssImageBean image = rss.getImage(); //Obtain the image element
           Vector items = rss.getItems(); //Obtain a Vector of item elements (RssItemBean)

           final DateFormat f = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss", Locale.ENGLISH);
           final DateFormat f1 = new SimpleDateFormat();

           
           // Iterate the list items
           for (int i=0; i<items.size(); i++){
                  RssItemBean item = (RssItemBean)items.elementAt(i); //Cast the Object from the list to RssItemBean
//                  System.out.println("Title: " + item.getTitle());
//                  System.out.println("Link : " + item.getLink());
//                  System.out.println("Desc.: " + item.getDescription());
                  
                  Date date;
                  try {
                      date = f.parse(item.getPubDate());
                  }
                  catch (ParseException e) {
                      try {
                          date = f1.parse(item.getPubDate());
                      }
                      catch (ParseException e1) {
                          date = new Date();
                      }
                  }
                  
                  NMessage n = new NMessage(item.getLink(), item.getAuthor(), url, date, item.getTitle(), item.getDescription());
                  n.setID(getMessageIDPrefix() + item.getLink());
                  n.addTag(item.getCategory());
                  if (image!=null) {
                      String u = image.getUrl();
                      if (u!=null)
                        if (u.length() > 0)
                            n.setImage(u);
                  }
                  onNewMessage(item, n);
                  m.add(n);                  
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return m;
    }

    /** useful for adding annotations to NMessage, parsed from the RSS item */
    public void onNewMessage(RssItemBean item, NMessage n) {
    }

    @Override
    public String toString() {
        return "RSSChannel(" + url + ")";
    }

    public String getMessageIDPrefix() {
        return "rss";
    }

    
    
}
