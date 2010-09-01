/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo.surface;

import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.ui.Button;
import java.awt.Color;

/**
 *
 * @author seh
 */
public class DemoButton extends AbstractSurfaceDemo {

    @Override
    public String getName() {
        return "2D Fractal Surface";
    }

    @Override
    public String getDescription() {
        return "Zoomable fractal 2D surface.  Multiple adjustable layers.  Adjustable control logic.";
    }

    public DemoButton() {
        super();

        add(new GridRect(6, 6));

        /* add rectangles, testing:
        --position
        --size
        --color
        --tilt
         */
        int numRectangles = 16;
        float maxRadius = 0.1f;
        for (int i = 0; i < numRectangles; i++) {
            float s = 1.0f + (float) Math.random() * maxRadius;
            float a = (float) i / 2.0f;
            float x = (float) Math.cos(a) * s;
            float y = (float) Math.sin(a) * s;

            Button r2 = new Button() {

                final Vec4f red = new Vec4f(Color.RED);
                final Vec4f blue = new Vec4f(Color.BLUE);
                final Vec4f yellow = new Vec4f(Color.YELLOW);

                @Override
                public Vec4f getBackgroundColor() {
                    if (isPressed()) {
                        return blue;
                    } else {
                        if (isTouched()) {
                            return red;
                        } else {
                            return yellow;
                        }
                    }

                }
            };

            r2.move(x, y, 0);
            r2.scale(0.6f, 0.3f);
            r2.tilt(a);
            add(r2);

        }

        add(new PointerLayer(this));
    }

    public static void main(String[] args) {
        new SwingWindow(AbstractSurfaceDemo.newPanel(new DemoButton()), 800, 800, true);
    }
}
