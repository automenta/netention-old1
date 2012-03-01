/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.map;

import java.awt.Color;

/**
 * a single rectangle, ex: one coordinate in a grid 
 * draws with alpha opacity to form a heatmap
 * 
 * 
 * @author seh
 */
public class GridRectMarker extends LabeledMarker {

    public GridRectMarker(Color c, double lat, double lng, int width, int height) {
        super(null, c, lat, lng);
        this.w = width;
        this.h = height;
    }

}
