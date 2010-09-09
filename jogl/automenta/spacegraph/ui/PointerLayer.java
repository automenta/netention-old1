/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.ui;

import automenta.spacegraph.Surface;
import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.shape.Drawable;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class PointerLayer implements Drawable {

    private final Surface canvas;
    final Vec3f color = new Vec3f();
    
    final Vec3f red = new Vec3f(1f, 0, 0);
    final Vec3f green  = new Vec3f(0, 1f, 0);
    
    float alpha = 0.75f;
   
    public PointerLayer(Surface canvas) {
        super();
        this.canvas = canvas;
    }

    public void draw(GL2 gl) {
        //gl.glMatrixMode(GL2ES1.GL_MODELVIEW);
        //gl.glLoadIdentity();
        //gl.glMatrixMode(GL2ES1.GL_PROJECTION);
        //gl.glLoadIdentity();
        
        drawPointer(gl, canvas.getPointer());
        //TODO handle multiple pointers
        
    }

    protected void drawPointer(GL2 gl, Pointer pointer) {
        gl.glOrtho(-1, 1, -1, 1, -1, 1);

        int numSteps = 20;
        double increment = Math.PI / numSteps;
        double radius = 4;

        float x = pointer.world.x();
        float y = pointer.world.y();

        if (pointer.buttons[0]) {
            color.lerp(green, 0.9f);
        }
         else {
            color.lerp(red, 0.9f);
         }
        
        gl.glBegin(GL2.GL_LINES);
        for (int i = numSteps - 1; i >= 0; i--) {
            gl.glColor4f(color.x(), color.y(), color.z(), alpha);
            gl.glVertex3d(x + radius * Math.cos(i * increment),
                y + radius * Math.sin(i * increment),
                0);
            gl.glVertex3d(x + -1.0 * radius * Math.cos(i * increment),
                y + -1.0 * radius * Math.sin(i * increment),
                0);
        }
        gl.glEnd();
    }
}
