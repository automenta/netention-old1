/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.survive;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.geo.Geo;
import automenta.netention.graph.Pair;
import automenta.netention.survive.DataInterest;
import automenta.netention.survive.DataSource;
import automenta.netention.survive.Environment;
import automenta.netention.survive.data.NuclearFacilities;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.JFloatSlider;
import automenta.netention.swing.widget.NowPanel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
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
public class SurvivalParametersPanel extends JPanel {

    JPanel configPanel = new JPanel();
    
    Map<String, Boolean> categoryVisible = new HashMap();
    final Map<DataSource, DataInterest> dataInterest = new HashMap();
    
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

    public DataInterest getInterest(DataSource ds) {
        DataInterest di = dataInterest.get(ds);        
        if (di == null) {
            di = new DataInterest(0, 0);
            dataInterest.put(ds, di);
        }
        return di;
    }
    public class DataSourcePanel extends JPanel {

        public DataSourcePanel(final MapDisplay map, final DataSource ds) {
            super();

            BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
            setLayout(bl);

            setAlignmentX(LEFT_ALIGNMENT);

            JLabel l = new JLabel(ds.name);
            try {
                l.setIcon(getIcon(ds.iconURL, datasourceIconWidth, datasourceIconHeight));
            } catch (MalformedURLException ex) {
                Logger.getLogger(SurvivalParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            add(l);

            final DataInterest di = getInterest(ds);

            //boolean layerEnabled = layerEnabled.get(ds);

            final JFloatSlider importanceSlider = new JFloatSlider(di.getImportance(), 0, 1.0, JSlider.HORIZONTAL);
            importanceSlider.setEnabled(false);
            importanceSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            di.setImportance(importanceSlider.value());
                            //map.getHeatMap().setInterestsChanged();
                            map.redraw();
                        }
                    }).start();
                }
            });
            JPanel fep = new JPanel(new FlowLayout());
            fep.setOpaque(false);
            fep.add(importanceSlider);
            add(fep);
            
//            DataRenderer dr = map.dataRenderers.get(ds);
//            if (dr instanceof ShadedCircleRenderer) {
//                final ShadedCircleRenderer scr = (ShadedCircleRenderer) dr;
//
//                JPanel ep = new JPanel(new FlowLayout());
//                ep.setOpaque(false);
//
//
//                final JToggleButton showEvents = new JToggleButton("Plot", layerEnabled);
//                ep.add(showEvents);
//
//                final JFloatSlider scaleSlider = new JFloatSlider(di.getScale(), scr.getMinScale(), scr.getMaxScale(), JSlider.HORIZONTAL);
//                scaleSlider.setEnabled(layerEnabled);
//                scaleSlider.addChangeListener(new ChangeListener() {
//
//                    @Override
//                    public void stateChanged(ChangeEvent e) {
//                        new Thread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                scr.setScale(scaleSlider.value());
//                                map.getHeatMap().setInterestsChanged();
//                                map.redraw();
//                            }
//                        }).start();
//                    }
//                });
//                ep.add(scaleSlider);
//
//                showEvents.addActionListener(new ActionListener() {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        map.setLayerEnabled(ds, showEvents.isSelected());
//                        scaleSlider.setEnabled(showEvents.isSelected());
//                    }
//                });
//                
//                ep.setAlignmentX(LEFT_ALIGNMENT);
//                add(ep);
//
//            }
         }
    }

    public SurvivalParametersPanel(Self self, Map2DPanel mp, Environment d) {
        super(new BorderLayout());

        this.self = self;
        this.map = mp;

        JPanel categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.PAGE_AXIS));
        
        HeatmapPanel hmp = new HeatmapPanel();
        categoriesPanel.add(hmp);

        int ic = 0;
        
        for (String s : d.categories) {
            JPanel c = new JPanel();
            c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

            final JPanel cSub = new JPanel();
            int indent = 15;
            int spacing = 5;
            cSub.setBorder(new EmptyBorder(spacing, indent, spacing, 0));
            cSub.setLayout(new BoxLayout(cSub, BoxLayout.PAGE_AXIS));
            {

                for (DataSource ds : d.getSources(s)) {
                    DataSourcePanel dsp = new DataSourcePanel(map, ds);
                    dsp.setBorder(new EmptyBorder(spacing, 0, 0, 0));
                    cSub.add(dsp);
                }
            }


            JLabel jb = new JLabel(s);

            try {
                ImageIcon ii = getIcon(d.categoryIcon.get(s), categoryImageWidth, categoryImageHeight);
                jb.setIcon(ii);
            } catch (MalformedURLException ex) {
                Logger.getLogger(SurvivalParametersPanel.class.getName()).log(Level.SEVERE, null, ex);
            }


            c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            c.add(jb, BorderLayout.NORTH);
            c.add(cSub, BorderLayout.CENTER);
            c.doLayout();

            c.setBorder(BorderFactory.createLoweredBevelBorder());

            categoriesPanel.add(c);
        }

        categoriesPanel.add(Box.createVerticalBox());

        add(new JScrollPane(categoriesPanel), BorderLayout.CENTER);


        JPanel presetsPanel = new JPanel(new BorderLayout());
        {
            JComboBox jc = new JComboBox();
            jc.addItem("Immediate Survival");
            jc.addItem("Hunteger-Gatherer");
            jc.addItem("Agriculture");
            jc.addItem("Outdoor Camping");
            jc.addItem("3rd World Urban");
            jc.addItem("1st World Urban");
            presetsPanel.add(jc, BorderLayout.CENTER);
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

            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        updateHeatmap();
                    }                    
                });
            }           
            
        });
        map.getMap().addJMVListener(new JMapViewerEventListener() {

            @Override
            public void processCommand(JMVCommandEvent jmvce) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        updateHeatmap();
                    }                    
                });
            }
            
        });

        NowPanel.redrawMarkers(self, map, null);
        hm = new HeatmapOverlay();
        map.getMap().addMapMarker(hm);
        
        updateHeatmap();
    }
    
    public double getThreat(final double lat, final double lng) {
        double t = 0;

        Iterator<Node> n = self.iterateNodes();
        while (n.hasNext()) {
            Node i = n.next();
            if (i instanceof Detail) {
                final Detail d = (Detail)i;
                
                if (d.hasPatternOr(NuclearFacilities.NuclearFacility) || self.isInstance("Disaster", d.getID())) {
                    double[] loc = Geo.getLocation(d);
                    double dist = Geo.meters((float)lat, (float)lng, (float)loc[0], (float)loc[1]);
                    double rad = 100 * 1000;
                    double scale = 0.02;
                    t += scale * 1.0 / (dist/rad);
                }
            }
            
        }
        
        if (t > 1.0) t = 1.0;
        
        return t;
    }
    
    public class HeatmapOverlay implements MapMarker {
        private int resolution;

        Map<Pair<Integer>, Double> threatValues = new ConcurrentHashMap();
        double minThreat = 0;
        double maxThreat = 0;
        int pw, ph;

        public HeatmapOverlay() {
        }
        

        public synchronized void update(int r) {
            this.resolution = r;
            
            int w = resolution;
            int h = resolution;
            
            pw = (int)Math.ceil( (float)map.getWidth() / (float)w);
            ph = (int)Math.ceil( (float)map.getHeight() / (float)h);
            
            
            threatValues.clear();
            
            
            //double lng = nw.getLon() + dLng/2.0;
            int px = 0;
            int py = 0;
            for (int x = 0; x < w; x++) {
                px = 0;
                for (int y = 0; y < h; y++) {
                    if (stop)
                        return;

                    Coordinate p = map.getMap().getPosition(px+pw/2, py+ph/2);
                    double lat = p.getLat();
                    double lng = p.getLon();
                    final double v = getThreat(lat, lng);
                    
                    if (threatValues.size()==0)
                        minThreat = maxThreat = v;
                    else {
                        if (v > maxThreat) maxThreat = v;
                        if (v < minThreat) minThreat = v;
                    }
                    
                    threatValues.put(new Pair<Integer>(px, py), v);                    
                    
                    px += pw;
                }

                //lng += dLng;
                py += ph;
            }
           
            map.getMap().repaint();
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
        public void paint(Graphics g, Point point) {

                
            if (minThreat!=maxThreat) {
                for (Entry<Pair<Integer>, Double> e : threatValues.entrySet()) {
                    if (stop)
                        return;

                    int ppx = e.getKey().getFirst();
                    int ppy = e.getKey().getSecond();

                    double normThreat = (e.getValue().doubleValue() - minThreat) / (maxThreat - minThreat);
                    final Color cc = new Color(1f, 0f, 0f, (float)normThreat);
                    g.setColor(cc);
                    g.fillRect(ppx, ppy, pw, ph);                    
                    
                }
            }

        }

        
    }
    
    boolean stop = false;
    Thread updater;
    
    public synchronized void updateHeatmap() {
        if (updater!=null) {
            stop = true;
            try {
                updater.interrupt();
                updater.stop();
                //updater.join();
            } catch (Exception ex) {
            }
        }
        
        
        updater = new Thread(new Runnable() {
            @Override
            public void run() {
                stop = false;
                
                
                for (int r = 6; r < 48; r+=2) {
                    if (stop)
                        return;
                    
                    hm.update(r);
                    
                    if (stop)
                        return;
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            }            
        });
        updater.start();
        
        
//       Coordinate center = this.map.getMap().getPosition();
//        Coordinate nw = this.map.getMap().getPosition(0, 0);
//        Coordinate se = this.map.getMap().getPosition(this.map.getWidth(), this.map.getHeight());
//
//        if (nw.equals(se))
//            return;
        
//        double mPerPx = this.map.getMap().getMeterPerPixel();
//        double pxPerM = 1.0 / mPerPx;
//        double mWide = Geo.meters((float)nw.getLat(), (float)nw.getLon(), (float)nw.getLat(), (float)se.getLon());
//        double mHigh = Geo.meters((float)se.getLat(), (float)nw.getLon(), (float)nw.getLat(), (float)nw.getLon());

        //int pw = (int)(mWide * pxPerM / ((float)cw));
        //int ph = (int)(mHigh * pxPerM / ((float)ch));
                
//        int pw = (int)Math.round((float)map.getWidth() / ((float)cw));
//        int ph = (int)Math.round((float)map.getHeight() / ((float)ch));
        
//        GeoRectScalarMap s = new GeoRectScalarMap(nw.getLat(), nw.getLon(), se.getLat(), se.getLon()) {
//            @Override public double value(double lat, double lng) {
//                return getThreat(lat, lng);
//            }                      
//        };
//        
//        GeoPointValue[] markers = s.get(cw, ch);

        //this.map.getMap().removeAllMapMarkers();
        
    }
    
}
