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

    public Rect() {
        super(new Vec3f(0,0,0), new Vec3f(1,1,1), new Vec3f(0,0,0));
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();

        transform(gl);


        final float w = 1f;
        final float h = 1f;

        // Six faces of cube
        // Top face
        gl.glPushMatrix();
        //gl.glColor3f(r, g, b);
        gl.glBegin(GL2.GL_QUADS);
        {
            //Front
                gl.glNormal3f(0, 0, 1); {
                    gl.glVertex3f(-w, -h, 0);
                    gl.glVertex3f(w, -h, 0);
                    gl.glVertex3f(w, h, 0);
                    gl.glVertex3f(-w, h, 0);
                }
        }
        gl.glEnd();



        gl.glPopMatrix();
    }

}
