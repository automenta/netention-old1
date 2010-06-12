/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.layer;

import automenta.spacegraph.Surface;
import automenta.spacegraph.shape.Drawable;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class PointerLayer implements Drawable {

    private final Surface canvas;
   
    public PointerLayer(Surface canvas) {
        super();
        this.canvas = canvas;
    }

    public void draw(GL2 gl) {
        //gl.glMatrixMode(GL2ES1.GL_MODELVIEW);
        //gl.glLoadIdentity();
        //gl.glMatrixMode(GL2ES1.GL_PROJECTION);
        //gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -1, 1);


        int numSteps = 20;
        double increment = Math.PI / numSteps;
        double radius = 4;

        float x = canvas.getPointer().pixel.x()/500.0f;
        float y = canvas.getPointer().pixel.y()/500.0f;

        gl.glBegin(GL2.GL_LINES);
        for (int i = numSteps - 1; i >= 0; i--) {
            gl.glColor3f(0.9f, 0.5f, 0.5f);
            gl.glVertex3d(x + radius * Math.cos(i * increment),
                y + radius * Math.sin(i * increment),
                -5);
            gl.glVertex3d(x + -1.0 * radius * Math.cos(i * increment),
                y + -1.0 * radius * Math.sin(i * increment),
                -5);
        }
        gl.glEnd();
    }
}
