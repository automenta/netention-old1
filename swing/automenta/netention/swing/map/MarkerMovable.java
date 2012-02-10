/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.map;

import java.awt.Point;

/**
 *
 * @author me
 */
public interface MarkerMovable {
 
    public Point getPaintPoint();
    public void setOffset(int offX, int offY);
    
}
