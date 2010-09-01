/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo.surface;

import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Repeat;
import automenta.spacegraph.ui.PointerLayer;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import java.awt.Color;

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
        return "Tests intersection with a Rect.";
    }

    public DemoDragPan() {
        super();

        //add(new GridRect(6, 6));

        /* add rectangles, testing:
        --position
        --size
        --color
        --tilt
         */
        float a = 0.5f;
        float x = 2;
        float y = 0;

        final Rect r2 = new Rect() {

            @Override
            public void onTouchChange(Pointer pointer, boolean touched) {
                color(touched ? new Vec4f(Color.MAGENTA) : new Vec4f(Color.ORANGE));
            }
            
        }.color(new Vec4f(Color.ORANGE));

        add(new Repeat() {
            @Override
            public void update(double dt, double t) {
                r2.tilt(r2.getTilt() + 0.04f);
                float s = (float)Math.sin(t*4.0) + 2.0f;
                r2.scale(1.5f * s, s);
            }
        });
        
        r2.move(x, y, 0);
        r2.scale(0.9f, 0.4f);
        r2.tilt(a);
        add(r2);

        add(new PointerLayer(this));
    }

    public static void main(String[] args) {
        new SwingWindow(new DemoDragPan().newPanel(), 800, 800, true);
    }
}
