/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.map;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * defines a bounded range upon which a marker is clickable to receive a click event if clicked inside it
 * @author me
 */
public interface MarkerClickable {

    public Rectangle getClickableArea();
    
    public void onMouseEnter(Point p);
    public void onMouseExit();
    public void onClicked(Point p, int button);
    
}
