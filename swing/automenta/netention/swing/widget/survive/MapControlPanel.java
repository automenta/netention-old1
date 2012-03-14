/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.survive;

import automenta.netention.*;
import automenta.netention.feed.TwitterChannel;
import automenta.netention.geo.Geo;
import automenta.netention.graph.Pair;
import automenta.netention.survive.*;
import automenta.netention.survive.data.OSMxml;
import automenta.netention.swing.Icons;
import automenta.netention.swing.map.LabeledMarker;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.JFloatSlider;
import automenta.netention.value.geo.GeoPointIs;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * Controls for determining (in real-time) the parameters of 'threat' and 'benefit' composite indicators which are visualized on a map as a heatmap overlay
 * @author seh
 */
public class MapControlPanel extends JPanel {
    final static Logger logger = Logger.getLogger(MapControlPanel.class.toString());
            
    JPanel configPanel = new JPanel();
        
    final static int categoryImageWidth = 40;
    final static int categoryImageHeight = 40;
    final static int datasourceIconWidth = 25;
    final static int datasourceIconHeight = 25;

    public static String getIconPath(String s) {
        return "./media/survive/" + s;
    }

    public static ImageIcon getIcon(String s, int w, int h) throws MalformedURLException {
        final URL u = new File(getIconPath(s)).toURL();
        ImageIcon ii = new ImageIcon(new ImageIcon(u).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        return ii;
    }
    private final Map2DPanel map;
    private final Self self;
    private final HeatmapOverlay hm;
    private final JPanel categoriesPanel;
    private final JFloatSlider opacitySlider;
    private Date focusDate;
    
    private JButton getOSM;

    public class HeatmapPanel extends JPanel {

        public HeatmapPanel() {
            super();

            BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
            setLayout(bl);

            setAlignmentX(LEFT_ALIGNMENT);

            JPanel ep = new JPanel(new FlowLayout());

            
//            boolean layerEnabled = heatmap.getLayer().isEnabled();
            boolean layerEnabled = false;

            //final JToggleButton showEvents = new JToggleButton("Analysis", layerEnabled);
            //ep.add(showEvents);

//            final JFloatSlider js = new JFloatSlider(heatmap.getOpacity(), 0, 1.0, JSlider.HORIZONTAL);
//            js.setEnabled(layerEnabled);
//            js.addChangeListener(new ChangeListener() {
//                @Override
//                public void stateChanged(ChangeEvent e) {
//                    heatmap.setOpacity(js.value());
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            map.redraw();
//                        }                        
//                    });
//                }
//            });
//            ep.add(js);

//            showEvents.addActionListener(new ActionListener() {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    heatmap.getLayer().setEnabled(showEvents.isSelected());
//                    js.setEnabled(showEvents.isSelected());
//                }
//            });

            ep.setAlignmentX(LEFT_ALIGNMENT);
            add(ep);
        }

    }

//    
    private SurvivalModel survive;
    
    public void addIndicator(final Influence i) {
                
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        final JPanel cSub = new JPanel();
        int indent = 15;
        int spacing = 5;
        cSub.setBorder(new EmptyBorder(spacing, indent, spacing, 0));
        cSub.setLayout(new BoxLayout(cSub, BoxLayout.PAGE_AXIS));

//            {
//
//                for (DataSource ds : d.getSources(s)) {
//                    DataSourcePanel dsp = new DataSourcePanel(map, ds);
//                    dsp.setBorder(new EmptyBorder(spacing, 0, 0, 0));
//                    cSub.add(dsp);
//                }
//            }

        final JFloatSlider importance = new JFloatSlider(0, 0, 1.0, JSlider.HORIZONTAL);
        final JFloatSlider radius = new JFloatSlider(100, 0.1, 1000, JSlider.HORIZONTAL);

        importance.setPaintLabels(true);
        radius.setPaintLabels(true);
        
        final JLabel importanceLabel = new JLabel();
        final JLabel radiusLabel = new JLabel();
        
        ChangeListener cl = new ChangeListener() {

            @Override public void stateChanged(ChangeEvent e) {
                i.importance = importance.value();
                i.radius = radius.value() * 1000.0;
                
                importanceLabel.setText("Importance: " + (int)(i.importance * 100.0) + "%");
                radiusLabel.setText("Radius: " + (int)(i.radius/1000.0) +  " km");
                
                boolean activated = i.importance!=0.0;
                
                radiusLabel.setVisible(activated);
                radius.setVisible(activated);
                radius.setEnabled(activated);
                
                if (e!=null)
                    updateOverlaysOnSwing();
            }
            
        };
                
        cSub.add(importanceLabel);
        importance.addChangeListener(cl);
        cSub.add(importance);
        
        cSub.add(radiusLabel);
        radius.addChangeListener(cl);
        cSub.add(radius);

        JLabel jb = new JLabel(i.label);
        jb.setToolTipText(i.affect.toString());

        ImageIcon ii = Icons.getPatternIcon(self.getPattern(i.icon));
        jb.setIcon(ii);


        c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        c.add(jb, BorderLayout.NORTH);
        c.add(cSub, BorderLayout.CENTER);
        c.doLayout();

        c.setBorder(BorderFactory.createLoweredBevelBorder());

        categoriesPanel.add(c);
        
        cl.stateChanged(null);
        
    }
    
    public MapControlPanel(Self self, Map2DPanel mp, Environment d) {
        super(new BorderLayout());

        this.self = self;
        this.map = mp;
        this.survive = new DefaultHeuristicSurvivalModel(self);
        hm = new HeatmapOverlay();

        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.PAGE_AXIS));
        
        final HeatmapPanel hmp = new HeatmapPanel();
        categoriesPanel.add(hmp);

        for (Influence i : survive.getInfluences()) {
            addIndicator(i);
        } 
        
//        for (String s : d.categories) {
//            JPanel c = new JPanel();
//            c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
//
//            final JPanel cSub = new JPanel();
//            int indent = 15;
//            int spacing = 5;
//            cSub.setBorder(new EmptyBorder(spacing, indent, spacing, 0));
//            cSub.setLayout(new BoxLayout(cSub, BoxLayout.PAGE_AXIS));
//            {
//
//                for (DataSource ds : d.getSources(s)) {
//                    DataSourcePanel dsp = new DataSourcePanel(map, ds);
//                    dsp.setBorder(new EmptyBorder(spacing, 0, 0, 0));
//                    cSub.add(dsp);
//                }
//            }
//
//
//            JLabel jb = new JLabel(s);
//
//            try {
//                ImageIcon ii = getIcon(d.categoryIcon.get(s), categoryImageWidth, categoryImageHeight);
//                jb.setIcon(ii);
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(SurvivalParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//
//            c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//            c.add(jb, BorderLayout.NORTH);
//            c.add(cSub, BorderLayout.CENTER);
//            c.doLayout();
//
//            c.setBorder(BorderFactory.createLoweredBevelBorder());
//
//            categoriesPanel.add(c);
//        }

        categoriesPanel.add(Box.createVerticalBox());

        add(new JScrollPane(categoriesPanel), BorderLayout.CENTER);


        JPanel presetsPanel = new JPanel(new BorderLayout());
        {
            JComboBox jc = new JComboBox();
            jc.addItem("Immediate Survival");
            jc.addItem("Hunter-Gatherer");
            jc.addItem("Agriculture");
            jc.addItem("Outdoor Camping");
            jc.addItem("3rd World Urban");
            jc.addItem("1st World Urban");
            presetsPanel.add(jc, BorderLayout.CENTER);
            
            
            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
            {
                getOSM = new JButton("Update OSM");
                getOSM.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateOSM();
                    }                    
                });
                optionsPanel.add(getOSM);
                
                JButton getTweets = new JButton("Update Tweets");
                getTweets.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateTwitter();
                    }                    
                });
                optionsPanel.add(getTweets);
                
                opacitySlider = new JFloatSlider(hm.getOpacity(), 0.0, 1.0, JSlider.HORIZONTAL);
                opacitySlider.setToolTipText("Heatmap Transparency");
                opacitySlider.addChangeListener(new ChangeListener() {
                    @Override public void stateChanged(ChangeEvent e) {
                        hm.setOpacity((float)opacitySlider.value());
                    }                
                });
                optionsPanel.add(new JLabel("Opacity"));
                optionsPanel.add(opacitySlider);

                final JLabel al = new JLabel("Date: ");
                final JFloatSlider ageSlider = new JFloatSlider(10000.0, 0.0, 10000.0, JSlider.HORIZONTAL);                
                ChangeListener agecl = new ChangeListener() {
                    @Override public void stateChanged(ChangeEvent e) {
                        Date date = new Date(new Date().getTime() - (long)((10000.0 - ageSlider.value()) * (60*60*24)));
                        al.setText("Date: " + date.toString());                        
                        setDate(date);
                    }                
                };
                ageSlider.addChangeListener(agecl);
                agecl.stateChanged(null);
                
                optionsPanel.add(al);
                optionsPanel.add(ageSlider);
            }
            presetsPanel.add(optionsPanel, BorderLayout.SOUTH);
        }
        add(presetsPanel, BorderLayout.NORTH);

        
        JPanel renderPanel = new JPanel(new BorderLayout());
        {
            JButton buyButton = new JButton("What may I need?");
            renderPanel.add(buyButton, BorderLayout.CENTER);
        }
        add(renderPanel, BorderLayout.SOUTH);

        doLayout();
        
        
        map.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                updateOverlaysOnSwing();
            }
        });
        map.getMap().addJMVListener(new JMapViewerEventListener() {
            @Override public void processCommand(JMVCommandEvent jmvce) {
                updateOverlaysOnSwing();                
            }            
        });

        redrawAll();
    }
    
    public void redrawAll() {
        redrawMarkers(null);
        map.getMap().addMapMarker(hm);
        
        updateOverlays();        
    }
    
    public void redrawMarkers(final java.util.List<Pattern> p) {        
        
        map.getMap().removeAllMapMarkers();

        final Iterator<Node> in;
        if (p!=null) {
            in = Iterators.filter(self.iterateNodes(), new Predicate<Node>() {
                @Override public boolean apply(Node t) {
                        
                    for (final Pattern x : p) {
                        if (self.isInstance(x.getID(), t.getID())) {
                            return true;  
                        }
                    }
                    
                    return false;
                }                
            });
        }
        else {
            in = self.iterateNodes();
        }
            
        while (in.hasNext()) {
            Detail d = (Detail)in.next();
            if (d.getMode() == Mode.Real) {
                if (self.isInstance("Located", d.getID())) {
                    addMarker(d);
                }
            }
        }
            
        
    }
    
    public void addMarker(final Detail d) {
        double[] l = Geo.getLocation(d);
                
        if (l!=null) {
            LabeledMarker m = new LabeledMarker(d.getName(), new Color(0.0f, 1.0f, 0.0f, 0.3f), l[0], l[1], Icons.getObjectIcon(self, d)) {

                @Override
                public float getOpacity() {
                    final float f = (float)DefaultHeuristicSurvivalModel.getAgeFactor(self, d, focusDate.getTime(), 50000);
                    return f;
                }
                
            };
            map.getMap().addMapMarker(m);        
        }
    }
    
    public boolean isOSMPossible() {
        Coordinate topLeft = map.getMap().getPosition(0,0);
        Coordinate botRight = map.getMap().getPosition(map.getWidth(), map.getHeight());
        
        double wl = topLeft.getLat() - botRight.getLat();
        double wh = botRight.getLon() - topLeft.getLon();
        
        return ((wl <= 0.25) && (wh <= 0.25));        
    }

    
    protected synchronized void updateTwitter() {
        self.queue(new Runnable() {

            @Override
            public void run() {
                int cx = map.getMap().getWidth() / 2;
                int cy = map.getMap().getHeight() / 2;
                Coordinate c = map.getMap().getPosition(cx, cy);
                double lat = c.getLat();
                double lng = c.getLon();
                
                try {
                    double radius = 8.0;
                    logger.info("Requesting tweets at " + lat +"," + lng + " at radius " + radius + " km");
                    java.util.List<Detail> nt = TwitterChannel.getTweets(lat, lng, radius);
                    for (Detail d : nt) {
                        self.addDetail(d);
                    }
                    logger.info("Received " + nt.size() + "  tweets");
                    redrawAll();
                } catch (Exception ex) {
                    Logger.getLogger(MapControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                                    
            }
        });
        
    }
    
    protected synchronized void updateOSM() {
        
        self.queue(new Runnable() {

            @Override
            public void run() {
                //http://wiki.openstreetmap.org/wiki/Download
                //The region is specified by a bounding box, which consists of a minimum and maximum latitude and longitude. Choose as small a region as will be useful to you, since larger regions will result in larger data files, longer download times, and heavier load on the server. The server may reject your region if it is larger than 1/4 degree in either dimension. When you're first starting out, choose a very small region so that you can figure things out quickly with small data sets.

                Coordinate topLeft = map.getMap().getPosition(0,0);
                Coordinate botRight = map.getMap().getPosition(map.getWidth(), map.getHeight());

                //min lng, min lat, max lng, max lat
                //ex: http://api.openstreetmap.org/api/0.6/map?bbox=11.54,48.14,11.543,48.145
                //ex: http://www.informationfreeway.org/api/0.6/node[amenity=hospital][bbox=-6,50,2,61]

                if (isOSMPossible()) {
                    String url = "http://api.openstreetmap.org/api/0.6/map?bbox=" + topLeft.getLon() + "," + botRight.getLat() + "," + botRight.getLon() + "," + topLeft.getLat();
                    //String url = "http://www.informationfreeway.org/api/0.6/node[bbox=" + topLeft.getLon() + "," + botRight.getLat() + "," + botRight.getLon() + "," + topLeft.getLat() + "]";
                    try {

                        final LinkedList<Detail> detailsAdded = new LinkedList();

                        new OSMxml(url) {

                            @Override
                            public void onAmenity(String id, double lat, double lon, String type) {
                                Detail d = new Detail(type, Mode.Real, "Located", "Built");
                                d.setID(id);
                                d.add("currentLocation", new GeoPointIs(lat, lon));

                                detailsAdded.add(d);
                                self.addDetail(d);
                            }

                        };

                        if (detailsAdded.size() > 0) {        
                            redrawAll();
                        }

                    }
                    catch (Exception ex) {
                        logger.severe(ex.toString());
                    }

                }
                else {
                    logger.severe("Area too large to request OSM data: " + topLeft + ".." + botRight);
                }
            }
            
        });
        
    }
    
    public void setDate(Date d) {
        this.focusDate = d;
        updateOverlaysOnSwing();
    }
    
    public class HeatmapOverlay implements MapMarker {
        private int resolution;

        Map<Pair<Integer>, double[]> values = new ConcurrentHashMap();
        double minThreat = 0, maxThreat = 0;
        double minBenefit = 0, maxBenefit = 0;
        int pw, ph;
        private float opacity = 0.75f;

                
        public HeatmapOverlay() {
        }
        

        public synchronized void update(int r) {
            this.resolution = r;
            
            int w = resolution;
            int h = resolution;
            
            pw = (int)Math.ceil( (float)map.getWidth() / (float)w);
            ph = (int)Math.ceil( (float)map.getHeight() / (float)h);
                        
            values.clear();
                        
            int px = 0;
            int py = 0;
            for (int x = 0; x < w; x++) {
                px = 0;
                for (int y = 0; y < h; y++) {
                    if (stopUpdater)
                        return;

                    final Coordinate p = map.getMap().getPosition(px+pw/2, py+ph/2);
                    final double lat = p.getLat();
                    final double lng = p.getLon();
        
                    double[] d = new double[3];
                    survive.get(lat, lng, d, null);

                    final double threat = d[0];
                    final double benefit = d[1];
                    final double certainty = d[2];
                    
                    if (values.isEmpty()) {
                        minThreat = maxThreat = threat;
                        minBenefit = maxBenefit = benefit;
                    }
                    else {
                        if (threat > maxThreat) maxThreat = threat;
                        if (threat < minThreat) minThreat = threat;
                        
                        if (benefit > maxBenefit) maxBenefit = benefit;
                        if (benefit < minBenefit) minBenefit = benefit;
                    }
                    
                    values.put(new Pair<Integer>(px, py), d);
                    
                    px += pw;
                }

                py += ph;
            }
           
            map.getMap().repaint();
        }

        private void setOpacity(float value) {
            if (value!=opacity) {
                this.opacity = value;
                map.getMap().repaint();
            }
            
        }
        
        @Override
        public double getLat() {
            return map.getMap().getPosition().getLat();
        }

        @Override
        public double getLon() {
            return map.getMap().getPosition().getLon();
        }

        @Override
        public void paint(final Graphics g, final Point point) {
            
            for (final Entry<Pair<Integer>, double[]> e : values.entrySet()) {
                if (stopUpdater)
                    return;

                final double normThreat = (maxThreat!=minThreat) ? (e.getValue()[0] - minThreat) / (maxThreat - minThreat) : 0; 
                final double normBenefit = (maxBenefit!=minBenefit) ? (e.getValue()[1] - minBenefit) / (maxBenefit - minBenefit) : 0;
                final double certainty = e.getValue()[2];
                
                final int ppx = e.getKey().getFirst();
                final int ppy = e.getKey().getSecond();

                final float cr = (float)normThreat;
                final float cg = (float)normBenefit;
                final float ca = opacity * Math.min(1.0f, (float)certainty);

                if (ca > 0) {                    
                    g.setColor(new Color(cr, cg, 0f, ca));
                    g.fillRect(ppx, ppy, pw, ph);        
                }

            }

        }

        private float getOpacity() {
            return opacity;
        }

        
    }
    
    
    boolean stopUpdater = false;
    Thread updater;
    
    public synchronized void updateOverlays() {
        if (updater!=null) {
            stopUpdater = true;
            try {
                updater.interrupt();
                //updater.stop();
                updater.join();
            } catch (Exception ex) {
            }
        }
        
        
        updater = new Thread(new Runnable() {
            @Override
            public void run() {
                stopUpdater = false;
                
                final int startResolution = 4;
                final int maxResolution = 64;
                final int resInc = 4;
                
                for (int r = startResolution; r < maxResolution; r+=resInc) {
                    if (stopUpdater)
                        return;
                    
                    hm.update(r);
                    
                    if (stopUpdater)
                        return;
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        return;
                    }
                    //Thread.yield();
                }
            }            
        });
        updater.start();
                
    }
    
    protected void updateOverlaysOnSwing() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                updateOverlays();
                
                getOSM.setEnabled(isOSMPossible());
            }                    
        });
    }
            
}

//    public DataInterest getInterest(DataSource ds) {
//        DataInterest di = dataInterest.get(ds);        
//        if (di == null) {
//            di = new DataInterest(0, 0);
//            dataInterest.put(ds, di);
//        }
//        return di;
//    }
//    
//    public class DataSourcePanel extends JPanel {
//
//        public DataSourcePanel(final MapDisplay map, final DataSource ds) {
//            super();
//
//            BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
//            setLayout(bl);
//
//            setAlignmentX(LEFT_ALIGNMENT);
//
//            JLabel l = new JLabel(ds.name);
//            try {
//                l.setIcon(getIcon(ds.iconURL, datasourceIconWidth, datasourceIconHeight));
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(SurvivalParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            add(l);
//
//            final DataInterest di = getInterest(ds);
//
//            //boolean layerEnabled = layerEnabled.get(ds);
//
//            final JFloatSlider importanceSlider = new JFloatSlider(di.getImportance(), 0, 1.0, JSlider.HORIZONTAL);
//            importanceSlider.setEnabled(false);
//            importanceSlider.addChangeListener(new ChangeListener() {
//
//                @Override
//                public void stateChanged(ChangeEvent e) {
//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            di.setImportance(importanceSlider.value());
//                            //map.getHeatMap().setInterestsChanged();
//                            map.redraw();
//                        }
//                    }).start();
//                }
//            });
//            JPanel fep = new JPanel(new FlowLayout());
//            fep.setOpaque(false);
//            fep.add(importanceSlider);
//            add(fep);
//            
////            DataRenderer dr = map.dataRenderers.get(ds);
////            if (dr instanceof ShadedCircleRenderer) {
////                final ShadedCircleRenderer scr = (ShadedCircleRenderer) dr;
////
////                JPanel ep = new JPanel(new FlowLayout());
////                ep.setOpaque(false);
////
////
////                final JToggleButton showEvents = new JToggleButton("Plot", layerEnabled);
////                ep.add(showEvents);
////
////                final JFloatSlider scaleSlider = new JFloatSlider(di.getScale(), scr.getMinScale(), scr.getMaxScale(), JSlider.HORIZONTAL);
////                scaleSlider.setEnabled(layerEnabled);
////                scaleSlider.addChangeListener(new ChangeListener() {
////
////                    @Override
////                    public void stateChanged(ChangeEvent e) {
////                        new Thread(new Runnable() {
////
////                            @Override
////                            public void run() {
////                                scr.setScale(scaleSlider.value());
////                                map.getHeatMap().setInterestsChanged();
////                                map.redraw();
////                            }
////                        }).start();
////                    }
////                });
////                ep.add(scaleSlider);
////
////                showEvents.addActionListener(new ActionListener() {
////
////                    @Override
////                    public void actionPerformed(ActionEvent e) {
////                        map.setLayerEnabled(ds, showEvents.isSelected());
////                        scaleSlider.setEnabled(showEvents.isSelected());
////                    }
////                });
////                
////                ep.setAlignmentX(LEFT_ALIGNMENT);
////                add(ep);
////
////            }
//         }
//    }
//
