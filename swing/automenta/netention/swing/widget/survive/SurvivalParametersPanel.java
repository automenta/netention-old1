/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.survive;

import automenta.netention.Self;
import automenta.netention.graph.Pair;
import automenta.netention.survive.*;
import automenta.netention.swing.Icons;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.JFloatSlider;
import automenta.netention.swing.widget.NowPanel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
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
    final static Logger logger = Logger.getLogger(SurvivalParametersPanel.class.toString());
            
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
                    updateHeatmapOnSwing();
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
    
    public SurvivalParametersPanel(Self self, Map2DPanel mp, Environment d) {
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
            
            
            opacitySlider = new JFloatSlider(hm.getOpacity(), 0.0, 1.0, JSlider.HORIZONTAL);
            opacitySlider.setToolTipText("Heatmap Transparency");
            opacitySlider.addChangeListener(new ChangeListener() {
                @Override public void stateChanged(ChangeEvent e) {
                    hm.setOpacity((float)opacitySlider.value());
                }                
            });
            presetsPanel.add(opacitySlider, BorderLayout.SOUTH);
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
                updateHeatmapOnSwing();
            }
        });
        map.getMap().addJMVListener(new JMapViewerEventListener() {
            @Override public void processCommand(JMVCommandEvent jmvce) {
                updateHeatmapOnSwing();
            }            
        });

        NowPanel.redrawMarkers(self, map, null);
        map.getMap().addMapMarker(hm);
        
        updateHeatmap();
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
        
                    double[] d = new double[2];
                    survive.get(lat, lng, d, null);

                    final double threat = d[0];
                    final double benefit = d[1];
                    
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

                final int ppx = e.getKey().getFirst();
                final int ppy = e.getKey().getSecond();

                final float cr = (float)normThreat;
                final float cg = (float)normBenefit;
                final float ca = opacity * Math.min(1.0f, cr + cg);

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
    
    public synchronized void updateHeatmap() {
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
    
    protected void updateHeatmapOnSwing() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                updateHeatmap();
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
