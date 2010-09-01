/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph;

import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Touchable;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.shape.Drawable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author seh
 */
public class DefaultSurface extends Surface {

    private final Space2D defaultSpace;

    public DefaultSurface() {
        super();
        defaultSpace = new Space2D();
        setSpace(defaultSpace);
    }

    public <D extends Drawable> D add(D d) {
        defaultSpace.add(d);
        return d;
    }

    public boolean remove(Drawable d) {
        return defaultSpace.remove(d);
    }

    @Override
    protected synchronized void handleTouch(Pointer p) {
        super.handleTouch(p);

        Set<Touchable> touchingNow = new HashSet();

        final Vec2f v = new Vec2f(p.world.x(), p.world.y());
        for (Drawable d : defaultSpace.getLayers()) {
            if (d instanceof Touchable) {
                Touchable t = (Touchable) d;
                if (t.isTouchable()) {
                    if (t.intersects(v)) {
                        touchingNow.add(t);
                    }
                }
            }
        }
        
        for (Touchable t : touchingNow) {
            if (!p.touching.contains(t)) {
                t.onTouchChange(p, true);
                p.touching.add(t);
            }
        }
        
        List<Touchable> toRemove = new LinkedList();
        for (Touchable t : p.touching) {
            if (!touchingNow.contains(t)) {
                t.onTouchChange(p, false);
                toRemove.add(t);
            } else {
                t.onTouchChange(p, true);                
            }
        }
        for (Touchable t : toRemove) {
            p.touching.remove(t);
        }
    }
}
