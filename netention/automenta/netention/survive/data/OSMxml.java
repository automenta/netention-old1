/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive.data;

import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses an OpenStreetMaps data file (XML)
 * @author SeH
 */
abstract public class OSMxml extends DefaultHandler {
    public static final Logger logger = Logger.getLogger(OSMxml.class.toString());
    
    //TODO public OSMDocument(Coordinate p1, Coordinate p2) { ... } 
    
    public OSMxml(String filePath) throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        //final BZip2CompressorInputStream bz = new BZip2CompressorInputStream(new FileInputStream(filePath));

        logger.info(this + " Loading: " + filePath);
        saxParser.parse(filePath, this);

    }

 
    double lat, lon;
    String id;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

//                  System.out.println(localName + " " + qName + " " + attributes.getLength());

        //System.out.println(qName);

        if (qName.equalsIgnoreCase("node")) {
            lat = Double.parseDouble(attributes.getValue("lat"));
            lon = Double.parseDouble(attributes.getValue("lon"));
            id = attributes.getValue("id");
            //TODO timestamp
            //                        for (int i = 0; i < attributes.getLength(); i++) {
            //                            String key = attributes.getQName(i);
            //                            String value = attributes.getValue(i);
            //                            System.out.println("  " + key + " " + value);
            //                        }

            
                
            //}
            
        
        }
        
        //if ((lat >= minLat) && (lat <= maxLat) && (lon >= minLon) && (lon <= maxLon)) {
        if (qName.equalsIgnoreCase("tag")) {
            //System.out.println(node.keySet().toString() + " " + attributes.keySet().toString());

            //System.out.println(lat + ", " + lon + "," + attributes.getValue("v"));
            //System.out.println(lat + ", " + lon + "," + " " + attributes.getValue("v"));
//            for (int i = 0; i < attributes.getLength(); i++) {
//                String k = attributes.getQName(i);
//                String v = attributes.getValue(k);
//                //System.out.println("  " + k + " " + v);
//            }

            if (attributes.getValue("k").equalsIgnoreCase("amenity")) {
                //System.out.println(lat + "," + lon + ": " + attributes.getValue("v"));
                final String v = attributes.getValue("v");
                onAmenity(id + "/" + v, lat, lon, v);
            }
        }

    }

    abstract public void onAmenity(String id, double lat, double lon, String type);
}
