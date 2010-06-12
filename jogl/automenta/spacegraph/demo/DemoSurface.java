/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo;

import automenta.netention.swing.RunDemos.Demo;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.Surface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.layer.GridRect;
import automenta.spacegraph.shape.Rect;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author seh
 */
public class DemoSurface extends DefaultSurface implements Demo {

    @Override
    public String getName() {
        return "2D Fractal Surface";
    }

    @Override
    public String getDescription() {
        return "Zoomable fractal 2D surface.  Multiple adjustable layers.  Adjustable control logic.";
    }

    @Override
    public JPanel newPanel() {
        JPanel j = new JPanel(new BorderLayout());
        {
            DemoSurface dc = new DemoSurface();
            SGPanel sdc = new SGPanel(dc);

            new FractalControl(sdc);

            final ControlRigPanel crp = new ControlRigPanel(dc, 0.25f);
            new Thread(crp).start();

            j.add(sdc, BorderLayout.CENTER);
            j.add(crp, BorderLayout.SOUTH);
        }
        return j;
    }

    public static class ControlRigPanel extends JPanel implements Runnable {

        private final JTextField pixelPointerText;
        private final JTextField worldPointerText;
        private final Surface surface;
        private final JTextField camText;
        private final long delay;

        public ControlRigPanel(Surface surface, float updateTime) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            this.delay = (long) (updateTime * 1000.0f);
            this.surface = surface;

            pixelPointerText = new JTextField();
            add(pixelPointerText);
            worldPointerText = new JTextField();
            add(worldPointerText);
            camText = new JTextField();
            add(camText);

            update();
        }

        public void update() {
            pixelPointerText.setText("Pointer Pixel: " + surface.getPointer().pixel.toString());
            worldPointerText.setText("Pointer World: not impl");
            camText.setText("Cam: " + surface.getCamera().camPos);
        }

        @Override
        public void run() {
            while (true) {
                update();

                try {
                    Thread.sleep(delay);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public DemoSurface() {
        super();

        GridRect gr = new GridRect();
        gr.scale(6, 6, 1);
        add(gr);
        
        add(new Rect().color(0.1f, 0.2f, 0.6f));

        Rect r2 = new Rect().color(0.6f, 0.2f, 0.1f);
        r2.move(0.5f, 0, 0);
        r2.scale(0.5f, 0.5f, 1.0f);
        add(r2);
    }

    public static void main(String[] args) {
        new SwingWindow(new DemoSurface().newPanel(), 800, 800, true);
    }
}
