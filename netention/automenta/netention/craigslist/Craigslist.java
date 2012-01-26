/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.craigslist;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Craigslist.org cities and categories
 * @author seh
 */
public class Craigslist implements Serializable {
    final static Logger logger = Logger.getLogger(Craigslist.class.toString());
    
    public static final transient String jsonPath = "schema/craigslist.json";
    public static transient Craigslist the = null;
    
    private final Map<String, String> categories;
    private final Map<String, String> locations;

    public Craigslist() {
        super();
        categories = new HashMap();
        locations = new HashMap();
    }

    
    public Craigslist(String path) throws IOException {
        this();
        JSONDeserializer<Craigslist> j = new JSONDeserializer();
        FileReader wr = new FileReader(path);
        j.deserializeInto(wr, this);
        wr.close();        
    }
    
    public Craigslist(Map<String, String> cityURL, Map<String, String> catURL) {
        super();
        this.locations = cityURL;
        this.categories = catURL;
    }

    public void save() throws IOException {
        save(jsonPath);
    }
    
    public void save(String path) throws IOException {
        JSONSerializer serializer = new JSONSerializer();

        FileWriter wr = new FileWriter(path);
        serializer.include("cities", "categories").prettyPrint(true).deepSerialize(this, wr);
        wr.close();        
    }
    
    /** Name -> URL */
    public Map<String, String> getLocations() {
        return locations;
    }
    
    /** Name -> URL */
    public Map<String, String> getCategories() {
        return categories;
    }

    /** returns the cached Craigslist instance, and attempts to loading it from the default JSON path if 'the' is currently null */
    public static Craigslist get() {
        if (the==null) {
            try {            
                the = new Craigslist(jsonPath);
            }
            catch (IOException e) {
                logger.info("Unable to find " + jsonPath + " ... creating blank Craigslist data");
                the = new Craigslist();
            }
        }
        
        return the;
    }
}
