/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.shape;

import automenta.spacegraph.math.linalg.Vec3f;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class Rect extends Spatial implements Drawable {

    Vec3f backgroundColor = new Vec3f(0.5f, 0.5f, 0.5f);

    boolean filled = true;

    public Rect() {
        super();
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public Rect color(float r, float g, float b) {
        return color(new Vec3f(r, g, b));
    }

    public Rect color(Vec3f c) {
        setBackgroundColor(c);
        return this;
    }

    public void setBackgroundColor(Vec3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vec3f getBackgroundColor() {
        return backgroundColor;
    }
    

    public void draw(GL2 gl) {
        if (isFilled()) {
            gl.glPushMatrix();

            transform(gl);


            final float w = 0.5f;
            final float h = 0.5f;

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
    }

    /** draw within -1..+1 for x, y */
    protected void drawFront(GL2 gl) {

    }
}
