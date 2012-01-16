/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.craigslist;

import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
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
