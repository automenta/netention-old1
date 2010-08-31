/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.control;

import automenta.spacegraph.SG.KeyStates;
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
    private Vec3f targetPos = new Vec3f(0, 0, 10);
    private Vec3f targetTarget = new Vec3f(0, 0, 0);
    private Vec3f targetUp = new Vec3f(0, 1, 0);
    private final Surface surface;
    float camLerp = 0.95f;
    float tiltLerp = 0.75f;
    float panSpeed = 0.01f;
    float wheelDZ = 4.0f;
    float tiltAngle = (float) Math.PI / 2.0f;
    float tiltSpeed = 0.002f;

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
        targetUp.set((float) Math.cos(tiltAngle), (float) Math.sin(tiltAngle), 0);

        surface.getCamera().camUp.lerp(targetUp, tiltLerp);

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

        float dx = -(xAng - downPixel.x());
        float dy = yAng - downPixel.y();

        if (surface.keyStates.get(KeyStates.CONTROL)) {
            tiltAngle += dy* tiltSpeed;
        } else {

            //rotate by current tiltAngle
            float nx = dx * (float) Math.cos(tiltAngle - (float) Math.PI / 2.0f) - dy * (float) Math.sin(tiltAngle - (float) Math.PI / 2.0f);
            float ny = dx * (float) Math.sin(tiltAngle - (float) Math.PI / 2.0f) + dy * (float) Math.cos(tiltAngle - (float) Math.PI / 2.0f);

            Vec3f delta = new Vec3f(nx, ny, 0);

            delta.scale(getPanSpeed());

            targetPos.set(downPointPos);
            targetTarget.set(downPointTarget);
            targetPos.add(delta);
            targetTarget.add(delta);
        }
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
