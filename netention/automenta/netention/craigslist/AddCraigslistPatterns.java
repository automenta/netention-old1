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
    public static final String CraigslistChannel = "CraigslistChannel";
    public static final String CraigslistItem = "CraigslistItem";
    public static final String CraigslistLocationProperty = "Craigslist_Location";
    public static final String CraigslistCategoryProperty = "Craigslist_Category";
    
    private Craigslist cl;

    public AddCraigslistPatterns() {
        super();
        
        try {
            this.cl = Craigslist.get();
        }
        catch (Exception e) {
            System.err.println(this + " " + cl);
            //TODO use logger
        }
    }

    
    public void add(Self s) {
        Pattern c;
        s.addPattern(c = new Pattern(CraigslistChannel, AddDefaultPatterns.web).withName("Craigslist Channel"));
        s.addPattern(new Pattern(CraigslistItem, AddDefaultPatterns.web).withName("Craigslist Ad"));
        
        SelectionProp loc = new SelectionProp(CraigslistLocationProperty, "Craigslist Location", new ArrayList(cl.getLocations().keySet()));        
        loc.setCardinalityMax(-1);
        loc.setCardinalityMin(1);
        s.addProperty(loc, c.getID());
        
        SelectionProp cat = new SelectionProp(CraigslistCategoryProperty, "Craigslist Category", new ArrayList(cl.getCategories().keySet()));        
        cat.setCardinalityMax(-1);
        cat.setCardinalityMin(1);
        s.addProperty(cat, c.getID());
        
    }   
}
