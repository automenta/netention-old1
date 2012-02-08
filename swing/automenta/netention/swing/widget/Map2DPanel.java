/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.swing.widget.survive.MapDisplay;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.OsmTileLoader;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

/**
 *
 * @author seh
 */
public class Map2DPanel extends JPanel implements JMapViewerEventListener, MapDisplay  {

    private static final long serialVersionUID = 1L;

    private JMapViewer map = null;

    private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    private JLabel mperpLabelName=null;
    private JLabel mperpLabelValue = null;
    private final JPanel optionsPanel;

    public Map2DPanel() {
        super();

        map = new JMapViewer();
        map.addJMVListener(new JMapViewerEventListener() {

            @Override
            public void processCommand(JMVCommandEvent jmvce) {
//                COMMAND cmd = jmvce.getCommand();
            }
        });
        map.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Coordinate p = map.getPosition(e.getPoint());
                if (e.getButton() == MouseEvent.BUTTON3) {
                    onRightClick(p);
                }
                else if (e.getButton() == MouseEvent.BUTTON1) {
                    onLeftClick(p, e);
                }
            }

            
        });
        map.setPreferredSize(null);
        map.setMinimumSize(null);

        // Listen to the map viewer for user operations so components will
        // recieve events and update
        map.addJMVListener(this);

        // final JMapViewer map = new JMapViewer(new MemoryTileCache(),4);
        // map.setTileLoader(new OsmFileCacheTileLoader(map));
        // new DefaultMapController(map);

        setLayout(new BorderLayout());
        optionsPanel = new JPanel();
        JPanel helpPanel = new JPanel();

        mperpLabelName=new JLabel("Meters/Pixels: ");
        mperpLabelValue=new JLabel(String.format("%s",map.getMeterPerPixel()));

        zoomLabel=new JLabel("Zoom: ");
        zoomValue=new JLabel(String.format("%s", map.getZoom()));

        //add(panel, BorderLayout.NORTH);
        
//        add(helpPanel, BorderLayout.SOUTH);
//        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
//                + "left double click or mouse wheel to zoom.");
//        helpPanel.add(helpLabel);
        
        JButton button = new JButton("setDisplayToFitMapMarkers");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setDisplayToFitMapMarkers();
            }
        });
        JComboBox tileSourceSelector = new JComboBox(new TileSource[] { new OsmTileSource.Mapnik(),
                new OsmTileSource.TilesAtHome(), new OsmTileSource.CycleMap(), new BingAerialTileSource() });
        tileSourceSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                map.setTileSource((TileSource) e.getItem());
            }
        });
        JComboBox tileLoaderSelector;
        try {
            tileLoaderSelector = new JComboBox(new TileLoader[] { new OsmFileCacheTileLoader(map),
                    new OsmTileLoader(map) });
        } catch (IOException e) {
            tileLoaderSelector = new JComboBox(new TileLoader[] { new OsmTileLoader(map) });
        }
        tileLoaderSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                map.setTileLoader((TileLoader) e.getItem());
            }
        });
        map.setTileLoader((TileLoader) tileLoaderSelector.getSelectedItem());
        optionsPanel.add(tileSourceSelector);
        optionsPanel.add(tileLoaderSelector);
        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setSelected(map.getMapMarkersVisible());
        showMapMarker.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setMapMarkerVisible(showMapMarker.isSelected());
            }
        });
        optionsPanel.add(showMapMarker);
        final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
        showTileGrid.setSelected(map.isTileGridVisible());
        showTileGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setTileGridVisible(showTileGrid.isSelected());
            }
        });
        optionsPanel.add(showTileGrid);
        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setSelected(map.getZoomContolsVisible());
        showZoomControls.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setZoomContolsVisible(showZoomControls.isSelected());
            }
        });
        optionsPanel.add(showZoomControls);
        optionsPanel.add(button);

        optionsPanel.add(zoomLabel);
        optionsPanel.add(zoomValue);
        optionsPanel.add(mperpLabelName);
        optionsPanel.add(mperpLabelValue);

        add(map, BorderLayout.CENTER);

        //
//        map.addMapMarker(new MapMarkerDot(49.814284999, 8.642065999));
//        map.addMapMarker(new MapMarkerDot(49.91, 8.24));
//        map.addMapMarker(new MapMarkerDot(49.71, 8.64));
//        map.addMapMarker(new MapMarkerDot(48.71, -1));
//        map.addMapMarker(new MapMarkerDot(49.8588, 8.643));

        // map.setDisplayPositionByLatLon(49.807, 8.6, 11);
        // map.setTileGridVisible(true);
    }

    public JMapViewer getMap() {
        return map;
    }
    
    @Deprecated public static void main(String[] args) {
        // java.util.Properties systemProperties = System.getProperties();
        // systemProperties.setProperty("http.proxyHost", "localhost");
        // systemProperties.setProperty("http.proxyPort", "8008");
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(new Map2DPanel());
        jf.setSize(500,500);
        jf.setVisible(true);
    }

    private void updateZoomParameters() {
        if (mperpLabelValue!=null)
            mperpLabelValue.setText(String.format("%s",map.getMeterPerPixel()));
        if (zoomValue!=null)
            zoomValue.setText(String.format("%s", map.getZoom()));
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            updateZoomParameters();
        }
    }

    @Override
    public void redraw() {
    }
    
    public void onLeftClick(Coordinate p, MouseEvent e) {
    }
    public void onRightClick(Coordinate p) {
    }
    
}
