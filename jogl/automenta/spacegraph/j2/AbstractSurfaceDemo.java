/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.j2;

import automenta.netention.swing.RunDemos.Demo;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.SG;
import automenta.spacegraph.Surface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.impl.SGPanel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author me
 */
abstract public class AbstractSurfaceDemo extends DefaultSurface implements Demo {

    public static JPanel newPanel(Surface space) {
        JPanel j = new JPanel(new BorderLayout());
        {
            SGPanel sdc = new SGPanel(space);

            new FractalControl(sdc);

            final ControlRigPanel crp = new ControlRigPanel(space, 0.25f);
            new Thread(crp).start();

            j.add(sdc, BorderLayout.CENTER);
            j.add(crp, BorderLayout.SOUTH);
        }
        return j;
    }
    
        @Override
    public JPanel newPanel() {
        JPanel j = new JPanel(new BorderLayout());
        {
            DemoRectTilt dc = new DemoRectTilt();
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
            worldPointerText.setText("Pointer World: " + surface.getPointer().world.toString());
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

}
