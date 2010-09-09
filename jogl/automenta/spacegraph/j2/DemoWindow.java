/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.j2;

import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.shape.Curve;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import automenta.spacegraph.ui.Window;

/**
 *
 * @author seh
 */
public class DemoWindow extends AbstractSurfaceDemo {

    @Override
    public String getName() {
        return "Window";
    }

    @Override
    public String getDescription() {
        return "Window";
    }

    public DemoWindow() {
        super();

        add(new GridRect(6, 6));

        Window w1 = new Window();
        w1.scale(4, 3).moveTo(1, 1, 0);
        add(w1);
        Window w2 = new Window();
        w2.scale(2, 1).moveTo(-1, -1, 0);
        add(w2);
        Curve c = new Curve(w1, w2, 2);
        c.setLineWidth(6);
        c.setColor(0.5f, 0.5f, 0.5f);
        add(c);
        add(new PointerLayer(this));
    }

    public static void main(String[] args) {
        new SwingWindow(AbstractSurfaceDemo.newPanel(new DemoWindow()), 800, 800, true);
    }
}
