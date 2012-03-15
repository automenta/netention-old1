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
import automenta.netention.value.geo.GeoPointIs;
import automenta.netention.value.integer.IntegerIs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * IAEA Nuclear Facilities
 * @author seh
 */
public class NuclearFacilities { 
    private static final Logger logger = Logger.getLogger(NuclearFacilities.class.toString());

    public static final String NuclearFacility = "NuclearFacility";
    
    //http://www.google.com/fusiontables/DataSource?dsrcid=579353&search=nuclear&cd=5
    //Seems to be last updated on March 15 2011
    
    final static Date dateCollected = new GregorianCalendar(2011, 3, 15).getTime();
      
    public static void add(Self s, String csvPath) {
        //super("Nuclear Facilities", "Pollution", "nuclear.png", "Number of Reactors");
                
        if (csvPath == null) {
            //TODO load from web and store at csvPath            
            throw new UnsupportedOperationException(NuclearFacilities.class.toString() + " Fetching from web");
        }

        File f = new File(csvPath);
        Date lastModified = new Date(f.lastModified());

        if (s.getPattern(NuclearFacility) == null) {
            Pattern p = new Pattern(NuclearFacility, "Built");
            p.setIconURL("media://survive/nuclear.png");
            s.addPattern(p);
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String header = reader.readLine();
            //String[] headerTokens = header.split(",");

            int lines = 0;
            while (reader.ready()) {
                String line = reader.readLine().trim();
                String[] tokens = line.split(",");

                if (tokens.length < 5) {
                    break;
                }

                String country = tokens[0].trim().replace("\"", "");
                String name = tokens[1].trim().replace("\"", "");
                double lat = Double.parseDouble(tokens[2].trim().replace("\"", ""));
                double lng = Double.parseDouble(tokens[3].trim().replace("\"", ""));
                
                int totalReactors = Integer.parseInt(tokens[4].trim().replace("\"", ""));
                
                
                Detail md = new Detail(name, Mode.Real, NuclearFacility, "Located");
                try {
                    GeoPointIs g = new GeoPointIs(lat, lng, country);
                    md.add("currentLocation", g);
                }
                catch (NumberFormatException exx) {
                    exx.printStackTrace();
                }
                
                md.add("totalReactors", new IntegerIs(totalReactors));
                md.setWhen(dateCollected);
                s.addDetail(md);
                
                //System.out.println(md.getValues());
                //System.out.println(g.getCoordinates()[0] + " " + g.getCoordinates()[1]);
                
                lines++;
            }
        } catch (Exception e) {
            logger.severe(e.toString());
        }
        
    }
    
    public static void main(String[] args) {
        Self s = new MemorySelf();
        NuclearFacilities.add(s, "schema/IAEA_Nuclear_Facilities.csv");
    }

}
