/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.Mode;
import automenta.netention.NMessage;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.Icons;
import automenta.netention.swing.SelfSession;
import automenta.netention.swing.map.LabeledMarker;
import automenta.netention.swing.util.JScaledTextArea;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.value.string.StringIs;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
[10:35:43 PM EDT] SeH: so dashboard is going to include:
[10:35:59 PM EDT] SeH: 1. status update (w/ way to attach a picture)
[10:36:17 PM EDT] SeH: 2. a map (where you can reposition it to set your location, or eventually feed a GPS unit into)
[10:37:21 PM EDT] SeH: 5. graphs showing measurements of system data (ex: # of details, bytes in/out etc)
 * @author seh
 */
abstract public class NowPanel extends JPanel {

    
    private final MemorySelf self;
    private final SelfSession config;
    private final Map2DPanel map;
    //private int DEFAULT_HOME_ZOOM = 3;
    private MapMarker homeMarker;

    //edit icon and username=self.author
    private static class ProfileEditor extends JPanel {

        public ProfileEditor() {
            super(new BorderLayout());
            
        }
    }

    //[10:36:28 PM EDT] SeH: 3. inbox: list of notifications
    //[10:37:11 PM EDT] SeH: 4. list of ongoing activities
    public static class NotificationsPanel extends JPanel {
        private final MemorySelf self;

        public NotificationsPanel(MemorySelf self) {
            super(new BorderLayout());
            this.self = self;
        }
        
        
    }
    
    public class StatusUpdatePanel extends JPanel {
        private final JScaledTextArea text;

        public StatusUpdatePanel() {
            super(new BorderLayout());
            
            JButton b = new JButton(Icons.getIcon("person"));
            b.setToolTipText("Edit Profile");
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SwingWindow(new ProfileEditor(), 400, 300);
                }                
            });
            add(b, BorderLayout.WEST);
            
            text = new JScaledTextArea("", 2.0f);
            text.setRows(1);
            text.setToolTipText("Enter a status message here.  Hit Ctrl-enter to remember it and clear the text for your next status update.");
            add(text, BorderLayout.CENTER);
            text.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
                        if (e.getKeyCode() == KeyEvent.VK_ENTER)
                            updateStatus();
                }
                
                
            });
        }
        
        protected void updateStatus() {
            String s = text.getText();
            if (s.length() > 0) {
                Date when = new Date();
                MemoryDetail m = new MemoryDetail(s, Mode.Real, NMessage.StatusPattern, "Located");
                m.addValue("currentLocation", new StringIs(config.getCurrentLocation().getLat() + ", " + config.getCurrentLocation().getLon()));
                self.addDetail(m);
            }
            text.setText("");
        }
        
        
    }
    
    public NowPanel(MemorySelf self, final SelfSession config) {
        super(new BorderLayout(6,6));
        
        this.config = config;
        this.self = self;
        
        /*
         * Map right click:
         *      --Set My Location
         *      --Create Detail Here
         *      --...?
         * 
         */
        JButton homeButton;
        JPanel mapPanel = new JPanel(new BorderLayout());
        {
            JPanel mapControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            {
                homeButton = new JButton("Home");
                mapControlPanel.add(homeButton);
            }
            mapPanel.add(mapControlPanel, BorderLayout.NORTH);
        }
        map = new Map2DPanel() {

            @Override
            public void onLeftClick(final Coordinate p, MouseEvent e) {
                super.onLeftClick(p, e);
                JPopupMenu jp = new JPopupMenu();

                /*
                JMenuItem label = new JMenuItem(p.toString());
                label.setEnabled(false);
                jp.add(label);
                
                jp.addSeparator();
                 
                */
                
                JMenuItem setHome = new JMenuItem("Set as Home");
                setHome.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setHome(p);
                    }                    
                });
                jp.add(setHome);

                JMenuItem addDetail = new JMenuItem("New detail here...");
                addDetail.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addDetail(p);
                    }                    
                });
                jp.add(addDetail);
                
                //jp.addSeparator();
                
                
                jp.show(this, e.getX(), e.getY());
            }
            
        };
        homeButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                goHome();
            }                    
        });
        
        //map.getRootPane().setG
        mapPanel.add(map, BorderLayout.CENTER);
        
        add(mapPanel, BorderLayout.CENTER);
        
        add(new StatusUpdatePanel(), BorderLayout.SOUTH);
        
        setHome(config.getCurrentLocation());
    }
    
    public void setHome(Coordinate h) {
        if (h == null)
            return;
        
        config.setCurrentLocation(h);

        if (homeMarker!=null)
            map.getMap().removeMapMarker(homeMarker);
        
        //homeMarker = new MapMarkerDot(h.getLat(), h.getLon());
        homeMarker = new LabeledMarker("Home", new Color(0.0f, 1.0f, 0.0f, 0.3f), h.getLat(), h.getLon());
        
        map.getMap().addMapMarker(homeMarker);
        
    }
    
    abstract public void addDetail(Coordinate h);

    public void goHome() {
        Coordinate c = config.getCurrentLocation();                        
        map.getMap().setDisplayPositionByLatLon(c.getLat(), c.getLon(), 13);
    }
}
