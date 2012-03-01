/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.survive;

import automenta.netention.geo.Geo;
import automenta.netention.geo.GeoRectScalarMap;
import automenta.netention.geo.GeoRectScalarMap.GeoPointValue;
import automenta.netention.survive.DataInterest;
import automenta.netention.survive.DataSource;
import automenta.netention.survive.Environment;
import automenta.netention.swing.map.GridRectMarker;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.JFloatSlider;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;

/**
 *
 * @author seh
 */
public class DefineSurvivalPanel extends JPanel {

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
                Logger.getLogger(DefineSurvivalPanel.class.getName()).log(Level.SEVERE, null, ex);
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

    public DefineSurvivalPanel(Map2DPanel mp, Environment d) {
        super(new BorderLayout());

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
                Logger.getLogger(DefineSurvivalPanel.class.getName()).log(Level.SEVERE, null, ex);
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
                updateHeatmap();
            }           
            
        });
        map.getMap().addJMVListener(new JMapViewerEventListener() {

            @Override
            public void processCommand(JMVCommandEvent jmvce) {
                updateHeatmap();
            }
            
        });
        
        updateHeatmap();
    }
    
    public void updateHeatmap() {
        int cw = 16;
        int ch = 16;
        
//       Coordinate center = this.map.getMap().getPosition();
        Coordinate nw = this.map.getMap().getPosition(0, 0);
        Coordinate se = this.map.getMap().getPosition(this.map.getWidth(), this.map.getHeight());

        if (nw.equals(se))
            return;
        
//        double mPerPx = this.map.getMap().getMeterPerPixel();
//        double pxPerM = 1.0 / mPerPx;
//        double mWide = Geo.meters((float)nw.getLat(), (float)nw.getLon(), (float)nw.getLat(), (float)se.getLon());
//        double mHigh = Geo.meters((float)se.getLat(), (float)nw.getLon(), (float)nw.getLat(), (float)nw.getLon());

        //int pw = (int)(mWide * pxPerM / ((float)cw));
        //int ph = (int)(mHigh * pxPerM / ((float)ch));
                
        int pw = (int)Math.ceil((float)map.getWidth() / ((float)cw));
        int ph = (int)Math.ceil((float)map.getHeight() / ((float)ch));
        
        GeoRectScalarMap s = new GeoRectScalarMap(nw.getLat(), nw.getLon(), se.getLat(), se.getLon()) {
            @Override public double value(double lat, double lng) {
                return Math.random();
            }                      
        };
        
        GeoPointValue[] markers = s.get(cw, ch);

        this.map.getMap().removeAllMapMarkers();
        for (GeoPointValue g : markers) {        
            this.map.getMap().addMapMarker(new GridRectMarker(new Color(1f, 0f, 0f, (float)g.value), g.lat, g.lng, pw, ph));
        }
    }
    
}
