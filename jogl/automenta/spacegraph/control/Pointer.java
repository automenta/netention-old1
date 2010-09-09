package automenta.spacegraph.control;

import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec3f;
import java.util.HashSet;
import java.util.Set;

public class Pointer {

    public final Vec2f pixel = new Vec2f(0, 0);
    public final Vec3f world = new Vec3f(0, 0, 0);
    public final Set<Touchable> touching = new HashSet();
    public boolean[] buttons = new boolean[3];

    public final Vec3f dragStartworld = new Vec3f(0, 0, 0);
    public boolean dragging = false;

    public Touchable getSmallestTouched() {
        //TODO actually implement it, but for now just return the first entry in 'touching'
        if (touching.isEmpty())
            return null;
        return touching.iterator().next();
    }
}
