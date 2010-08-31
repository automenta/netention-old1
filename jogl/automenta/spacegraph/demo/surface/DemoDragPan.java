/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo.surface;

import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.layer.GridRect;
import automenta.spacegraph.layer.PointerLayer;
import automenta.spacegraph.shape.Rect;

/**
 *
 * @author seh
 */
public class DemoDragPan extends AbstractSurfaceDemo {

    @Override
    public String getName() {
        return "2D Fractal Surface";
    }

    @Override
    public String getDescription() {
        return "Zoomable fractal 2D surface.  Multiple adjustable layers.  Adjustable control logic.";
    }


    public DemoDragPan() {
        super();

        add(new GridRect(6, 6));
        
        add(new Rect().color(0.1f, 0.2f, 0.6f, 0.5f));

        /* add rectangles, testing:
                --position
                --size
                --color
                --tilt
         */
        int numRectangles = 16;
        float maxRadius = 0.1f;
        for (int i = 0; i < numRectangles; i++) {
            float s = 1.0f + (float)Math.random() * maxRadius;
            float a = (float)i/4.0f;
            float x = (float)Math.cos(a) * s;
            float y = (float)Math.sin(a) * s;
            
            Rect r2 = new Rect().color(0.6f, 0.2f, 0.1f, 0.5f);
            r2.move(x, y, 0);
            r2.scale(0.3f, 0.3f);
            r2.tilt(a);
            add(r2);
            
        }

        add(new PointerLayer(this));
    }
    
    

    public static void main(String[] args) {
        new SwingWindow(new DemoDragPan().newPanel(), 800, 800, true);
    }
}
