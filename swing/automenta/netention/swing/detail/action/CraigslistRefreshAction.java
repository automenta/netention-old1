/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail.action;

import automenta.netention.Detail;
import automenta.netention.NMessage;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.craigslist.AddCraigslistPatterns;
import automenta.netention.craigslist.Craigslist;
import automenta.netention.craigslist.CraigslistChannel;
import automenta.netention.swing.detail.DetailAction;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.value.set.SelectionEquals;
import automenta.netention.value.set.SelectionIs;
import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.horrabin.horrorss.RssItemBean;

/**
 *
 * @author seh
 */
public class CraigslistRefreshAction implements DetailAction {

    final Craigslist cl = Craigslist.get();

    final boolean complete;

    public CraigslistRefreshAction(boolean complete) {
        this.complete = complete;
    }
    
    @Override
    public boolean applies(Self s, Detail d) {
        if (d.getPatterns().contains(AddCraigslistPatterns.CraigslistChannel)) {
            if (getLocations(d).size() > 0)
                if (getCategories(d).size() > 0)
                    return true;
        }
        return false;
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
    public Runnable getRun(final Self s, final Detail d) {
        final List<String> locations = getLocations(d);
        final List<String> categories = getCategories(d);
        
        
        return new Runnable() {
            @Override
            public void run() {
                JPanel statusPanel = new JPanel(new BorderLayout());
                final JTextArea output = new JTextArea();
                output.setEditable(false);
                
                statusPanel.add(new JScrollPane(output), BorderLayout.CENTER);
                
                output.append("Locations: " + locations + "\n");
                output.append("Categories: " + categories + "\n\n");
                
                SwingWindow sw = new SwingWindow(statusPanel, 400, 300);
                sw.setTitle("Loading data from Craigslist.org... (closes when finished)");
                
                for (String loc : locations) {
                    for (String category: categories) {
                        output.append("Loading " + loc + " " + category + "...\n");
                        
                        String xloc = cl.getLocations().get(loc);
                        String xcategory = cl.getCategories().get(category);
                        
                        CraigslistChannel ccl = new CraigslistChannel(xloc, xcategory, complete) {

                            @Override
                            public void onNewMessage(RssItemBean item, NMessage n) {
                                super.onNewMessage(item, n);
                                output.append("  " + n.getSubject() + "\n");
                            }
                            
                        }; 
                        
                        for (NMessage m : ccl.getMessages()) {
                            s.addDetail(m);
                        }
                        
                    }
                }
                
                sw.setVisible(false);

            }            
        };
    }



}
