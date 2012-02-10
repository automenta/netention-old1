/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.map;

import java.awt.Graphics;
import java.awt.Point;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * a single rectangle, ex: one coordinate in a grid 
 * draws with alpha opacity to form a heatmap
 * 
 * 
 * @author seh
 */
public class GridRectMarker implements MapMarker {

    public GridRectMarker() {
    }

    
    @Override
    public double getLat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getLon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void paint(Graphics grphcs, Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
