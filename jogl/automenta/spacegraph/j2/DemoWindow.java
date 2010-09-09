/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.j2;

import automenta.netention.swing.util.SwingWindow;
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

        add(new Window().scale(4, 3).moveTo(1, 1, 0));
        add(new Window().scale(2, 1).moveTo(-1, -1, 0));
        
        add(new PointerLayer(this));
    }

    public static void main(String[] args) {
        new SwingWindow(AbstractSurfaceDemo.newPanel(new DemoWindow()), 800, 800, true);
    }
}
