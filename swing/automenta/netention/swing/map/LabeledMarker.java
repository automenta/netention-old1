/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.map;

import automenta.netention.swing.Icons;
import java.awt.*;
import javax.swing.ImageIcon;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * displays a marker and a small label near it
 * @author seh
 */
public class LabeledMarker implements MapMarker, MarkerClickable {
    private Rectangle clickableArea;
    private final double lat;
    private final double lon;
    private String text;
    private Color bgColor;
    private Color textColor = Color.BLACK;
    int w = 30;
    int h = 30;
    private ImageIcon icon;

    public LabeledMarker(String text, Color bgColor, double lat, double lon) {
        super();
        
        this.text = text;
        this.bgColor = bgColor;
        
        this.lat = lat;
        this.lon = lon;
    }
    public LabeledMarker(String text, Color bgColor, double lat, double lon, ImageIcon icon) {
        this(text, bgColor, lat, lon);
        setIcon(icon);
    }
    
    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    
    @Override
    public void paint(Graphics g, Point point) {
        
        g.setColor(bgColor);
        
        int cx = point.x - w/2;
        int cy = point.y - h/2;
        
        if (icon!=null) {
            g.drawImage(icon.getImage(), cx, cy, w, h, null);
        }
        else {
            g.fillRect(cx, cy, w, h);
        }
        
        g.setColor(textColor);
        
        g.setFont(g.getFont().deriveFont(18.0f)); //new Font("Sans", Font.BOLD, 18 )
        g.drawString(text, cx, cy+h);
        
        //update clickable area
        if (clickableArea == null) {
            clickableArea = new Rectangle();
        }
        clickableArea.setLocation(cx, cy);
        clickableArea.setSize(w, h);
        
    }

    @Override
    public Rectangle getClickableArea() {
        return clickableArea;
    }

    @Override
    public void onClicked(Point p, int button) {
        System.out.println(this + " Clicked!");
    }

    @Override
    public void onMouseEnter(Point p) {
        w = h = 35;        
    }

    @Override
    public void onMouseExit() {
        w = h = 30;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

}
