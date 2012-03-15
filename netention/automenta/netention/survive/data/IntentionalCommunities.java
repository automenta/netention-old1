/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive.data;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.Comment;
import automenta.netention.value.geo.GeoPointIs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * IC.org - Intentional Communities
 * 
 * http://directory.ic.org/maps/default.xml is ic.org's XML file used in their map, containing ~2300 datapoints 
 * which seems to be only a fraction of IC.org directory. 
 * 
 * scraping their directory is a next step to get all the contact information and details about each community
 * 
 * @author SeH
 */
public class IntentionalCommunities {
    private static final Logger logger = Logger.getLogger(IntentionalCommunities.class.toString());
    
    public static final String IntentionalCommunity = "IntentionalCommunity";
    
    int added = 0;
    
    public IntentionalCommunities(final Self self, String path) {
        this(self, path, -1);
    }
    
    public IntentionalCommunities(final Self self, String path, final int limit) {
        
        Pattern p = self.addPattern(new Pattern(IntentionalCommunity, "Built").withName("Intentional Community").setIconURL("media://tango32/places/user-home.png"));
        
	SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
     
            DefaultHandler handler = new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);

                    if (limit!=-1)
                        if (added == limit)
                            return;
                    
                    if (qName.equals("marker")) {
                        String label = attributes.getValue("label");
                        double lat = Double.valueOf(attributes.getValue("lat"));
                        double lng = Double.valueOf(attributes.getValue("lng"));
                        String desc = attributes.getValue("html");
                                                
                        Detail d = new Detail(label, Mode.Real, IntentionalCommunity, "Located" );
                        d.add(new Comment(desc));
                        d.add("currentLocation", new GeoPointIs(lat, lng));
                        self.addDetail(d);
                        
                        added++;
                    }
                    
                }
                
                
            };
            
            saxParser.parse(path, handler);
        } catch (Exception ex) {
            Logger.getLogger(IntentionalCommunities.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        logger.info(added + " communities loaded");
    }
    
    public static void main(String[] args) {
        Self ms = new MemorySelf();
        new IntentionalCommunities(ms, "schema/ic.org.xml");
    }
    
}
