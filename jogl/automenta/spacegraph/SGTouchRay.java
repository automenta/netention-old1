/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph;

import automenta.spacegraph.gleem.linalg.Vec2f;
import automenta.spacegraph.shape.Drawable;
import com.sun.opengl.util.BufferUtil;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author seh
 */
public class SGTouchRay implements Drawable {

    final Vec2f pixelPos;

    public SGTouchRay(Vec2f pixelPos) {
        super();
        this.pixelPos = pixelPos;
    }

    @Override
    public void draw(GL2 gl) {

        //drawScene(gl);
    }

}
