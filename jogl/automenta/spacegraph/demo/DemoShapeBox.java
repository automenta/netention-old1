/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo;

import automenta.spacegraph.SGCanvas;
import automenta.spacegraph.SGWindow;
import automenta.spacegraph.gleem.linalg.Vec3f;
import automenta.spacegraph.layer.MouseLayer;
import automenta.spacegraph.shape.Box;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class DemoShapeBox extends SGCanvas {

    private float textScaleFactor;
    float xAng = 0;
    float yAng = 0;
    private final Box box;

    public DemoShapeBox() {
        super();


        add(new MouseLayer(this));

        box = new Box();
        add(box);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        xAng = e.getX();
        yAng = e.getY();
    }

    @Override
    protected void updateSpace(GL2 gl) {
        xAng += 15 * (float) time.deltaT();
        yAng += 15 * (float) time.deltaT();

        //box.getCenter().set((float)Math.cos(getT()), (float)Math.sin(getT()), 0);
        //box.getSize().set(1.0f + (float)Math.cos(getT())/2f, 1.0f + (float)Math.sin(getT())/2f, 1.0f);
        box.getRotation().set(xAng, yAng, 0);        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            getCamera().camPos.add(new Vec3f(-0.1f, 0, 0));
            getCamera().camTarget.add(new Vec3f(-0.1f, 0, 0));
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            getCamera().camPos.add(new Vec3f(0.1f, 0, 0));
            getCamera().camTarget.add(new Vec3f(0.1f, 0, 0));
        }
    }

    public static void main(String[] args) {
        new SGWindow("DemoSGCanvas", new DemoShapeBox());
    }
}
