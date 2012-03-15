/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.craigslist;

import com.google.gson.Gson;
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


    public static Craigslist get(String path) throws IOException {
        FileReader wr = new FileReader(path);
        Craigslist cl = new Gson().fromJson(wr, Craigslist.class);        
        wr.close();
        return cl;
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

        FileWriter wr = new FileWriter(path);
        new Gson().toJson(this, wr);
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
    public static Craigslist get() throws IOException {
        return get(jsonPath);
//        if (the==null) {
//            try {            
//                the = new Craigslist(jsonPath);
//            }
//            catch (IOException e) {
//                logger.info("Unable to find " + jsonPath + " ... creating blank Craigslist data");
//                the = new Craigslist();
//            }
//        }
//        
//        return the;
    }
}
