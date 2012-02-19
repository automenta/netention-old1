/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.survive.data;

import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.rss.HTTP;
import automenta.netention.value.geo.GeoPointIs;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Emergency and Disaster Information Service interface
 * http://hisz.rsoe.hu
 * @author seh
 */
public class EDIS {
    final static Logger logger = Logger.getLogger(EDIS.class.toString());

    public static final String url = "http://hisz.rsoe.hu/alertmap/index2.php";

    public static final String baseURL = "http://hisz.rsoe.hu/alertmap/";

    public static Map<String,String> eventIcon = new HashMap();
    static {
     
        
    }
    
    public static String getPattern(String name) {
        return name.replace(" ", "_");
    }

    /**
     *  adds Disaster patterns
     */
    public EDIS init(Self self) {
        
        Pattern disaster;
        self.addPattern(disaster = new Pattern("Disaster"));
        
        self.addPattern(new Pattern(getPattern("Earthquake"), disaster.id).setIconURL("media://edis/DS_UGE.seism.png"));
        self.addPattern(new Pattern(getPattern("Volcano Activity Report"), disaster.id).setIconURL("media://edis/VOE.vulano_eruption.png"));
        
        return this;
    }
    
    
    public EDIS update(Self self) {
        
        //var point = new GLatLng(19.421, -155.287);var marker = createMarker(point,'<div style="width:240px" align="left"><b>Volcano Activity Report<\/b><br\/><br\/><b>Volcano name:<\/b> Kilauea<br\/><b>Location:<\/b> Hawaii and Pacific Ocean, HI<br\/><b>Alert level:<\/b> Watch<br\/><b>Alert code:<\/b> Orange<br\/><b>Last report:<\/b> -<br\/><a href="read/index.php?pageid=volcano_read&amp;number=1302-01-">Details<\/a><br\/><br\/><b><small><font color="blue">Report by USGS</font><\/div>', VOE);map.addOverlay(marker);
        String[] p = HTTP.getURL(url).split("\n");
        for (String s : p) {
            s = s.trim();
            s = s.replace("\\/", "/");
            
            if (!s.contains("new GLatLng("))
                continue;
            if (!s.contains("createMarker"))
                continue;
            
            int a1 = s.indexOf("(");
            int a2 = s.indexOf(")");
            String latlng = s.substring(a1+1, a2);
            
            int b1 = s.indexOf("'");
            int b2 = s.indexOf("'", b1+1);
            String text = s.substring(b1+1, b2);

            String name = null;
            String data = "";

            
            Document doc = Jsoup.parse(text);
            Elements e = doc.getElementsByTag("div").first().getAllElements();
            
            for (int i = 0; i < e.size(); i++) {
                Element x = e.get(i);
                if ((name == null) && ((x.tagName().equals("b")) || (x.tagName().equals("big"))) ) {
                    name = x.text();                        
                }
            }

            String ee = doc.getElementsByTag("div").html();
            String date = "", loc = "", country = "";
            
            List<String> datas = new LinkedList();
            for (String f : ee.split("<br />")) {
                f = f.trim();
                if (f.length() > 0) {
                    f = f.replace("<b>", "");
                    f = f.replace("</b>", "");
                    datas.add(f);
                    
                    final String datePrefix = "Date: ";
                    final String datePrefix2 = "Event date: ";
                    final String locPrefix = "Location: ";
                    final String countryPrefix = "Country: ";
                    if (f.startsWith(datePrefix)) {
                        date = f.substring(datePrefix.length());
                    }
                    else if (f.startsWith(datePrefix2)) {
                        date = f.substring(datePrefix2.length());
                    }
                    else if (f.startsWith(locPrefix)) {
                        loc =  f.substring(locPrefix.length());
                    }
                    else if (f.startsWith(countryPrefix)) {
                        country =  f.substring(countryPrefix.length());
                    }
                }
            }
            
            date = date.replace(" :" , "");
            date = date.replace(".", "-");
            
            //System.out.println(name + " " + latlng);
            //System.out.println("  " + date + " " + loc + " in " + country);
            //System.out.println("  " + datas);
            
            MemoryDetail d = new MemoryDetail(name, Mode.Real, getPattern(name));
            
            //TODO set detail to page that the EDIS marker refers to
            
            if (latlng.length() > 0) {
                try {                  
                    d.addValue("currentLocation", new GeoPointIs(latlng));
                    d.addPattern("Located");
                }
                catch (NumberFormatException exx) { 
                    logger.severe("Invalid geolocation: " + latlng);
                }
            }
//            else {
//                System.out.println("  missing location: " + ee);
//            }
            
            //TODO parse date            
                //d.setWhenCreated
            
            self.addDetail(d);
        }
        
        return this;
    }

    /** only needs to be called once, or each time EDIS changes */
    public static void getIcons() {
        //var FDS_TC_S1 = new GIcon(baseIcon, "icon/FH1.png",null, "icon/shadow_icon.png");        
        
        String[] p = HTTP.getURL(url).split("\n");
        for (String s : p) {
            s = s.trim();
            String xp = "new GIcon(baseIcon, \"";
            if (s.contains(xp)) {
                
                //System.out.println(s);
                
                String pl = s.substring(s.indexOf(xp) + xp.length());
                String iconURL = pl.substring(0, pl.indexOf("\""));
                
                final String var = s.split(" ")[1];
                
                final String iconName = iconURL.split("/")[1];
                
                iconURL = baseURL + iconURL;

                System.out.println(var + " " + iconName + " " + iconURL);

                try {
                    HTTP.saveURL(iconURL, "media/edis/" + var + "." + iconName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            }
            
        }
    }
    
//    public static void main(String[] args) {
//        Self s = RunSelfBrowser.newDefaultSelf();
//        new EDIS(s);
//    }
}
