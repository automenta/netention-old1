/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.app;

import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.survive.data.EDIS;
import automenta.netention.survive.data.IntentionalCommunities;
import automenta.netention.survive.data.NuclearFacilities;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.survive.MapControlPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author Sue
 */
public class RunSurvivalMap extends JPanel {
    private final JSplitPane split;

    public RunSurvivalMap(Self self) {
        super(new BorderLayout());
        
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(split, BorderLayout.CENTER);
        
        Map2DPanel map = new Map2DPanel();
        
        split.setRightComponent(new JScrollPane(map));
        
        split.setLeftComponent(new MapControlPanel(self, map, null));
        
    }
 
    
    
    public static void main(String[] args) {
        Self self = new MemorySelf();
        
        Pattern disaster; 
        self.addPattern(disaster = new Pattern("Disaster").setIconURL("media://edis/DS_VW.terror.png"));
            
        NuclearFacilities.add(self, "schema/IAEA_Nuclear_Facilities.csv");

        //new EDIS().init(self, disaster).update(self);
        
        //new IntentionalCommunities(self, "schema/ic.org.xml", 450);
        
        new SwingWindow(new RunSurvivalMap(self), 800, 600, true);
    }
    
}
