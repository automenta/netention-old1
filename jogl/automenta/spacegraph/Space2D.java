package automenta.spacegraph;

import automenta.spacegraph.shape.Drawable;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
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

        gl.glEnable(GL.GL_BLEND);			// Turn Blending On
        gl.glDisable(GL.GL_DEPTH_TEST);	// Turn Depth Testing Off
        //gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);					// Full Brightness.  50% Alpha (new )
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);					// Set The Blending Function For Translucency (new )

        for (Drawable d : layers) {
            //gl.glPushName(id++);
            d.draw(gl);
            //gl.glPopName();
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
