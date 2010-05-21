/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.shape;

import automenta.spacegraph.gleem.linalg.Vec3f;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class Rect extends Spatial implements Drawable {

    Vec3f backgroundColor = new Vec3f(1f, 0f, 1f);

    public Rect() {
        super();
    }

    public void setBackgroundColor(Vec3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vec3f getBackgroundColor() {
        return backgroundColor;
    }
    

    public void draw(GL2 gl) {
        gl.glPushMatrix();

        transform(gl);


        final float w = 1f;
        final float h = 1f;

        // Six faces of cube
        // Top face
        gl.glColor3f(backgroundColor.x(), backgroundColor.y(), backgroundColor.z());
        gl.glBegin(GL2.GL_QUADS);
        {
            //Front
                //gl.glNormal3f(0, 0, 1); {
                    gl.glVertex3f(-w, -h, 0);
                    gl.glVertex3f(w, -h, 0);
                    gl.glVertex3f(w, h, 0);
                    gl.glVertex3f(-w, h, 0);
                //}
        }
        gl.glEnd();

        drawFront(gl);

        gl.glPopMatrix();
    }

    /** draw within -1..+1 for x, y */
    protected void drawFront(GL2 gl) {

    }
}
