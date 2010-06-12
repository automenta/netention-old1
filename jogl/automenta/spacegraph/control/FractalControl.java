/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.control;

import automenta.spacegraph.Surface;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec3f;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author seh
 */
public class FractalControl implements MouseListener, MouseMotionListener, MouseWheelListener, Repeat {
    
    private Vec2f downPixel;
    private Vec3f downPointPos;
    private Vec3f downPointTarget;
    private Vec3f targetPos = new Vec3f(0,0,10);
    private Vec3f targetTarget = new Vec3f(0,0,0);
    private final Surface surface;
    float camLerp = 0.95f;
    float panSpeed = 0.01f;
    float wheelDZ = 4.0f;

    public FractalControl(SGPanel panel) {
        super();
        this.surface = panel.getSurface();
        surface.add(this);
        panel.getCanvas().addMouseListener(this);
        panel.getCanvas().addMouseMotionListener(this);
        panel.getCanvas().addMouseWheelListener(this);
    }

    @Override
    public void update(double dt, double t) {
        surface.getCamera().camPos.lerp(targetPos, camLerp);
        surface.getCamera().camTarget.lerp(targetTarget, camLerp);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        downPixel = new Vec2f(e.getX(), e.getY());
        downPointPos = new Vec3f(targetPos);
        downPointTarget = new Vec3f(targetTarget);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        float xAng = e.getX();
        float yAng = e.getY();


        Vec3f delta = new Vec3f(-(xAng - downPixel.x()), yAng - downPixel.y(), 0);
        delta.scale(getPanSpeed());

        targetPos.set(downPointPos);
        targetTarget.set(downPointTarget);
        targetPos.add(delta);
        targetTarget.add(delta);
    }

    public float getPanSpeed() {
        return panSpeed;
    }

    public float getWheelDZ() {
        return wheelDZ;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Vec3f delta = new Vec3f(0, 0, e.getWheelRotation() * getWheelDZ());
        targetPos.add(delta);
        targetTarget.add(delta);
    }


}
