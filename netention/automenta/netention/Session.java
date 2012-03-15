/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sue
 */
public class Session {
    
    private static Properties v = new Properties();
    private static final Logger logger = Logger.getLogger(Session.class.toString());

    public static void init() {
        final String iniFile = System.getProperty("user.home") + "/.netention.session";
        
        try {
            logger.info("loading from: " + iniFile);
            
            FileReader fr = new FileReader(iniFile);
            v.load(fr);
            fr.close();
            
        }
        catch (Exception e) {
        }
        
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override public void run() {
                try {
                    logger.info("saving to: " + iniFile);
                    
                    FileWriter fw = new FileWriter(iniFile);
                    v.store(fw, "");
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }));
    }
    
    public static String get(String property) {
        if (!v.containsKey(property)) {
            v.put(property, "");
            return "";
        }
        return v.getProperty(property);
    }
    
    public static void setValue(String property, String newValue) {
        v.put(property, newValue);
    }
    


}
