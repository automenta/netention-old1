/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.craigslist;

import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.demo.AddDefaultPatterns;
import automenta.netention.value.set.SelectionProp;
import java.util.ArrayList;

/**
 *
 * @author seh
 */
public class AddCraigslistPatterns {
    
    private final Craigslist cl;

    public AddCraigslistPatterns() {
        super();
        this.cl = Craigslist.get();
    }

    
    public void add(Self s) {
        Pattern c;
        s.addPattern(c = new Pattern("Craigslist", AddDefaultPatterns.web));
        
        SelectionProp loc = new SelectionProp("craigslist_location", "Craigslist Location", new ArrayList(cl.getLocations().keySet()));        
        loc.setCardinalityMax(-1);
        loc.setCardinalityMin(1);
        s.addProperty(loc, c.getID());
        
        SelectionProp cat = new SelectionProp("craigslist_category", "Craigslist Category", new ArrayList(cl.getCategories().keySet()));        
        cat.setCardinalityMax(-1);
        cat.setCardinalityMin(1);
        s.addProperty(cat, c.getID());
        
    }   
}
