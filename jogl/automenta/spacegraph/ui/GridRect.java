/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.ui;

import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.shape.Rect;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class GridRect extends Rect {

    float lineWidth = 2.0f;
    Vec3f lineColor = new Vec3f(1f, 1f, 1f);
    float dx = 0.1f;
    float dy = 0.1f;

    public GridRect(float sx, float sy) {
        super();
        scale(sx, sy, 1.0f);
        color(0,0,0);
        setFilled(false);
    }
    
       
    @Override public void draw(GL2 gl) {
        super.draw(gl);

        gl.glPushMatrix();

        transform(gl);
        
        gl.glEnable(gl.GL_LINE_SMOOTH);
        gl.glLineWidth(lineWidth);

        gl.glColor3f(lineColor.x(), lineColor.y(), lineColor.z());

        gl.glBegin(gl.GL_LINES);
        {
            for (float x = -0.5f; x < 0.5f ; x+=dx) {
                gl.glVertex3f(x, -0.5f, 0);
                gl.glVertex3f(x, 0.5f, 0);
            }
            for (float y = -0.5f; y < 0.5f ; y+=dy) {
                gl.glVertex3f(-0.5f, y, 0);
                gl.glVertex3f(0.5f, y, 0);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();

    }

}
