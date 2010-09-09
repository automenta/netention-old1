/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.ui;

import automenta.spacegraph.control.Draggable;
import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Pressable;
import automenta.spacegraph.control.Touchable;
import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.shape.Rect;

/**
 *
 * @author me
 */
public class Window extends Rect implements Touchable, Pressable, Draggable {

    private boolean touchable = true;
    private boolean pressable = true;
    private Vec3f dragStartPoint;
    private Vec3f dragOrigin;
    
    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @Override
    public void onTouchChange(Pointer pointer, boolean touched) {
    }

    @Override
    public void onPressChange(Pointer pointer, boolean pressed) {
    }

    @Override
    public boolean isPressable() {
        return pressable;
    }

    @Override
    public void onDragStart(Pointer pointer, Vec3f worldStart) {
        //System.out.println("drag start: " + worldStart);
        dragOrigin = new Vec3f(this.center);
        dragStartPoint = new Vec3f(worldStart);
    }

    @Override
    public void onDragging(Pointer pointer, Vec3f world) {
        //System.out.println("dragging: " + world);
        moveTo(world.x()-dragStartPoint.x()+dragOrigin.x(), world.y()-dragStartPoint.y()+dragOrigin.y(), world.z()-dragStartPoint.z()+dragOrigin.z());
    }

    @Override
    public void onDragEnd(Pointer pointer, Vec3f worldEnd) {
        //System.out.println("drag end: " + worldEnd);
    }

}
