package automenta.netention.craigslist;

import automenta.netention.NMessage;
import automenta.netention.rss.RSSChannel;
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
    private boolean complete = true;

    public CraigslistChannel(String loc, String category, boolean complete) {
        this(loc, category);
        this.complete = complete;
    }
    
    public CraigslistChannel(String loc, String category) {
        this(loc + "/" + category + "/index.rss");
    }

    public CraigslistChannel(String url) {
        super(url);
    }

    /** parsing CLTAG's */
    public void onNewMessage(RssItemBean item, NMessage n) {
        n.setName(item.getTitle());
        n.addPattern(AddCraigslistPatterns.CraigslistItem);
        
        if (complete) {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    public static String cityToBaseURL(String c) {
        return "http://" + c + ".craigslist.org";
    }
    
    public static void main(String[] args) {

        List<NMessage> l = new CraigslistChannel(cityToBaseURL("chicago"), "eng").getMessages();
        for (NMessage m : l) {
            System.out.println(m);
        }
    }
    
    @Override public String getMessageIDPrefix() {
        return "craigslist";
    }
    
}
