package automenta.spacegraph;

import automenta.spacegraph.shape.Drawable;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;

public class Space2D implements Drawable {

    List<Drawable> layers = new ArrayList();

    public Space2D() {
        super();
    }

    public Space2D(Drawable layer) {
        this();
        layers.add(layer);
    }

    public List<Drawable> getLayers() {
        return layers;
    }

    @Override
    public void draw(GL2 gl) {
        int id = 0;

        for (Drawable d : layers) {
            gl.glPushName(id++);
            d.draw(gl);
            gl.glPopName();
        }
    }

    public void removeAll() {
        layers.clear();
    }

    public <D extends Drawable> D add(D d) {
        layers.add(d);
        return d;
    }

    public boolean remove(Drawable d) {
        return layers.add(d);
    }

}
