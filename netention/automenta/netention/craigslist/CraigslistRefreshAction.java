/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.craigslist;

import automenta.netention.Detail;
import automenta.netention.NMessage;
import automenta.netention.PropertyValue;
import automenta.netention.action.DetailAction;
import automenta.netention.value.set.SelectionEquals;
import automenta.netention.value.set.SelectionIs;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.horrabin.horrorss.RssItemBean;

/**
 * if "complete" is enabled:
 * Fetches incomplete data fields (ex: author's e-mail address) for
 * a detail that is missing that information.  (Only appears
 * when the detail is a Craiglist Ad that is missing the author field)

 * @author seh
 */
public class CraigslistRefreshAction extends DetailAction {
    private static final Logger logger = Logger.getLogger(CraigslistRefreshAction.class.toString());
    
    final Craigslist cl = Craigslist.get();

    final boolean complete;

    public CraigslistRefreshAction(boolean complete) {
        super();
        this.complete = complete;
    }
    
    @Override
    public double applies(Detail d) {
        if (d.getPatterns().contains(AddCraigslistPatterns.CraigslistChannel)) {
            if (getLocations(d).size() > 0)
                if (getCategories(d).size() > 0)
                    return 1.0;
        }
        return 0.0;
    }

    @Override    
    public String getLabel() {
        if (complete)
            return "Refresh Craigslist (Complete)";
        else 
            return "Refresh Craigslist (Fast)";
    }

    @Override
    public String getDescription() {
        if (complete)
            return "Loads the latest Craigslist.org data associated with this detail, including the author's e-mail address";
        else
            return "Loads the latest Craigslist.org data associated with this detail";
    }

    public static String getString(PropertyValue pv) {
        if (pv instanceof SelectionIs) {
            SelectionIs i = (SelectionIs)pv;
            return i.getValue();
        }
        else if (pv instanceof SelectionEquals) {
            SelectionEquals i = (SelectionEquals)pv;
            return i.getValue();            
        }
        return null;
    }
    
    public static List<String> getLocations(Detail d) {
        List<String> locations = new LinkedList();
        for (PropertyValue pv : d.getValues()) {
            String p = pv.getProperty();
            
            if (p.equals(AddCraigslistPatterns.CraigslistLocationProperty)) {
                String location = getString(pv);
                if (location!=null) 
                    if (location.length() > 0)
                        locations.add(location);                
                
            }
        }
        return locations;        
    }
    
    public static List<String> getCategories(Detail d) {
        List<String> categories = new LinkedList();
        
        for (PropertyValue pv : d.getValues()) {
            String p = pv.getProperty();
            
            if (p.equals(AddCraigslistPatterns.CraigslistCategoryProperty)) {
                String category = getString(pv);
                if (category!=null) 
                    if (category.length() > 0)
                        categories.add(category);                
            }
        }
        
        return categories;                
    }
    
    @Override
    public Runnable getRun(final Detail d) {
        final List<String> locations = getLocations(d);
        final List<String> categories = getCategories(d);
        
        
        return new Runnable() {
            @Override
            public void run() {
                
                for (String loc : locations) {
                    for (String category: categories) {
                        
                        String xloc = cl.getLocations().get(loc);
                        String xcategory = cl.getCategories().get(category);
                        
                        CraigslistChannel ccl = new CraigslistChannel(xloc, xcategory, complete) {

                            @Override
                            public void onNewMessage(RssItemBean item, NMessage n) {
                                super.onNewMessage(item, n);
                                //output.append("  " + n.getSubject() + "\n");
                            }
                            
                        };                         
                        
                        Collection<NMessage> ms = ccl.getMessages();
                        NMessage[] mss = ms.toArray(new NMessage[ms.size()]);
                                
                        getSelf().addDetail(mss);
                        
                    }
                }
                

            }            
        };
    }



}
