package automenta.netention.craigslist;

import automenta.netention.NMessage;
import automenta.netention.rss.RSSChannel;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.horrabin.horrorss.RssItemBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides the latest Craigslist messages
 * @author seh
 */
public class CraigslistChannel extends RSSChannel {

    //ex: http://pittsburgh.craigslist.org/eng/index.rss
    public CraigslistChannel(String city, String category) {
        this("http://" + city + ".craigslist.org/" + category + "/index.rss");
    }

    public CraigslistChannel(String url) {
        super(url);
    }

    /** parsing CLTAG's */
    public void onNewMessage(RssItemBean item, NMessage n) {
        try {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //ex: <!-- CLTAG contract=on -->
        final String content = item.getDescription();

        final String prefix = "<!-- CLTAG";

        int i = 0;
        while ((i = content.indexOf(prefix, i)) != -1) {
            int end = content.indexOf("-->", i);
            String p = content.substring(i, end).substring(prefix.length());


            int e = p.indexOf('=');
            if (e != -1) {
                String k = p.substring(0, e);
                String v = p.substring(e + 1, p.length());

                //System.out.println(k + " = " + v);

                n.addTag("#" + k);
                n.addTag(v);
                n.addTag(p);
            } else {
                n.addTag(p);
            }

            i = end;
        }
    }

    public static void main(String[] args) {

        List<NMessage> l = new CraigslistChannel("chicago", "eng").getMessages();
        for (NMessage m : l) {
            System.out.println(m);
        }
    }
}
