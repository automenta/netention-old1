/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.*;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.swing.Icons;
import automenta.netention.swing.SelfSession;
import automenta.netention.swing.map.LabeledMarker;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.JScaledTextArea;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.value.geo.GeoPointIs;
import automenta.netention.value.string.StringIs;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
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

    
    private final Self self;
    private final SelfSession config;
    private final Map2DPanel map;
    //private int DEFAULT_HOME_ZOOM = 3;
    private MapMarker homeMarker;
    private final ItemTreePanel typeFilter;

    //edit icon and username=self.author
    private static class ProfileEditor extends JPanel {

        public ProfileEditor() {
            super(new BorderLayout());
            
        }
    }

    //[10:36:28 PM EDT] SeH: 3. inbox: list of notifications
    //[10:37:11 PM EDT] SeH: 4. list of ongoing activities
    public static class NotificationsPanel extends JPanel {
        private final Self self;

        public NotificationsPanel(Self self) {
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
    
    public NowPanel(final Self self, final SelfSession config) {
        super(new BorderLayout(6,6));
        
        this.config = config;
        this.self = self;
        
        /*
         * Map left click:
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
        
        mapPanel.add(map, BorderLayout.CENTER);
        
        add(mapPanel, BorderLayout.CENTER);
        
        typeFilter = new ItemTreePanel(self) {

            @Override
            public void onOpened(Object item) {
            }

            @Override
            public TreeModel getModel() {
                return new TypeTreeModel(self, false);
            }
            
        };
        
        typeFilter.refresh();
        typeFilter.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            @Override public void valueChanged(TreeSelectionEvent e) {
                redrawMarkers();
            }

        });
        
        add(new JScrollPane(typeFilter), BorderLayout.EAST);
        
        add(new StatusUpdatePanel(), BorderLayout.SOUTH);
        
        setHome(config.getCurrentLocation());
    

        redrawMarkers(null);
    }
    
    public void setHome(Coordinate h) {
        if (h == null)
            return;
        
        config.setCurrentLocation(h);

        
        //homeMarker = new MapMarkerDot(h.getLat(), h.getLon());
        homeMarker = new LabeledMarker("Home", new Color(0.0f, 1.0f, 0.0f, 0.3f), h.getLat(), h.getLon());
    
        redrawMarkers();
        
    }
    
    abstract public void addDetail(Coordinate h);

    public void goHome() {
        Coordinate c = config.getCurrentLocation();                        
        map.getMap().setDisplayPositionByLatLon(c.getLat(), c.getLon(), 13);
    }
    
    private void redrawMarkers(final List<Pattern> p) {        
        
        map.getMap().removeAllMapMarkers();

        if (homeMarker != null)
            map.getMap().addMapMarker(homeMarker);
        
        if (p!=null) {
            final Iterator<Node> in = Iterators.filter(self.iterateNodes(), new Predicate<Node>() {
                @Override public boolean apply(Node t) {
                        
                    for (final Pattern x : p) {
                        if (self.isInstance(x.getID(), t.getID())) {
                            return true;  
                        }
                    }
                    
                    return false;
                }                
            });
            
            while (in.hasNext()) {
                Detail d = (Detail)in.next();
                if (d.getMode() == Mode.Real) {
                    if (self.isInstance("Located", d.getID())) {
                        addMarker(d);
                    }
                }
            }
            
        }
    }
    
    public void addMarker(Detail d) {
        double[] l = GeoPointIs.getLocation(d);
                
        if (l!=null) {
            LabeledMarker m = new LabeledMarker(d.getName(), new Color(0.0f, 1.0f, 0.0f, 0.3f), l[0], l[1], Icons.getObjectIcon(self, d));
            map.getMap().addMapMarker(m);        
        }
    }
            

    public void redrawMarkers() {
        redrawMarkers(typeFilter.getSelectedPatterns());        
    }
}
