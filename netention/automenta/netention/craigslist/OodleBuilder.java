/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.craigslist;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.set.SelectionProp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author seh
 */
public class OodleBuilder {


    public void build(MemorySelf ms) throws Exception {
        //http://developer.oodle.com/files/xml/oodle_categories.xml
        Map<String,String> cat = new HashMap();
        
        BufferedReader br = new BufferedReader(new FileReader(new File(OodleBuilder.class.getResource("oodle_categories.xml").toURI())));
        while (br.ready()) {
            String l = br.readLine();
            String[] p = l.split("\"");
            if (p.length == 5) {
                String url = p[1].trim();
                String name = p[3].trim();
                //System.out.println(url + " " + name);
                cat.put(url, name);
                
            }
        }
        br.close();

        
        //Build Type Hierarchy ---------------
              
        for (int d = 1; d < 10; d++) {
            for (String s : cat.keySet()) {
                String[] c = s.split("/");
                if (c.length == d) {
                    Pattern p;
                    if (d == 1) {
                        p = new Pattern(s);
                        char startchar = s.charAt(0);
                        if (Character.isLowerCase(startchar)) {
                            p.setName(Character.toUpperCase(startchar) + s.substring(1));
                        }
                    }
                    else {
                        String parent = "";
                        for (int x = 0; x < d-1; x++) {
                            parent += c[x] + "/";
                        }
                        if (parent.endsWith("/"))
                            parent = parent.substring(0, parent.length() - 1);
                        
                        //System.out.println(s + " hasParent " + parent);
                        
                        p = new Pattern(s, parent);
                        p.setName(cat.get(s));          
                    }
                    ms.addPattern(p);
                }
            }
        }
        
        addAttributes(ms);
        
    }
    
    protected void addAttributes(MemorySelf ms) {
        //http://developer.oodle.com/attributes-list

        
        {
            /*
            ad_type

            Type of listing.

            Categories: 'housing/sale', 'housing/rent', 'job', 'sale', 'service', 'vehicle'

            Possible values:

            ad_type_found     // Found
            ad_type_lost      // Lost
            ad_type_wanted    // Wanted
            ad_type_adoption  // Adoption (pets only)
            ad_type_for_sale  // For sale (pets only)
             */

            List<String> values = new LinkedList();
            values.add("Found");
            values.add("Lost");
            values.add("Wanted");

            Property ad_type = new SelectionProp("ad_type", "Ad Type", values);
            ms.addProperty(ad_type, "housing/sale", "housing/rent", "job", "sale", "service", "vehicle");

        }
        {
            
        }
    }
    
    public static void main(String[] args) throws Exception {
        new OodleBuilder().build(new MemorySelf());
        
    }
    
//    public static void main(String[] args) throws Exception {
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//	SAXParser saxParser = factory.newSAXParser();
//        
//	DefaultHandler handler = new DefaultHandler() {
//
//            @Override
//            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//                super.startElement(uri, localName, qName, attributes);
//                System.out.println(qName + " " + attributes);
//            }
//
//            
//            @Override            
//            public void endElement(String uri, String localName, String qName) throws SAXException {
//                super.endElement(uri, localName, qName);
//            }
//
//            
//        };
//        
//        saxParser.parse(Oodle.class.getResource("oodle_categories.xml").openStream(), handler);
//    }
    
}
