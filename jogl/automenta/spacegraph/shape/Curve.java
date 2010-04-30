/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.shape;

import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class Curve extends Spatial implements Drawable {

    private final String label;
    private final Rect aRect;
    private final Rect bRect;
    float lineWidth = 2f;
    float lineSteps = 20.0f;
    int bezierPartitions = 20;
    private final TextRect labelRect;

    public Curve(String label, Rect aRect, Rect bRect) {
        super();
        this.label = label;
        this.aRect = aRect;
        this.bRect = bRect;
        this.labelRect = new TextRect(label);
    }

    @Override
    public void draw(GL2 gl) {

        final int nbCtrlPoints = 4;
        final int sizeCtrlPoints = nbCtrlPoints * 3;
        float ctrlPoints[] = {
            aRect.getCenter().x(), aRect.getCenter().y(), aRect.getCenter().z(),
            aRect.getCenter().x(), aRect.getCenter().y(), bRect.getCenter().z(),
            aRect.getCenter().x(), bRect.getCenter().y(), bRect.getCenter().z(),
            bRect.getCenter().x(), bRect.getCenter().y(), bRect.getCenter().z()
        };

        gl.glMap1f(gl.GL_MAP1_VERTEX_3,
            0.0f, 1.0f, 3,
            nbCtrlPoints, ctrlPoints, 0);

        gl.glEnable(gl.GL_MAP1_VERTEX_3);
        gl.glEnable(GL2.GL_DEPTH_TEST);


        gl.glPushMatrix();
        transform(gl);

// Draw ctrlPoints.
//        gl.glBegin(gl.GL_POINTS);
//        {
//            for (int i = 0; i < sizeCtrlPoints; i += 3) {
//                gl.glVertex3f(ctrlPoints[i],
//                    ctrlPoints[i + 1],
//                    ctrlPoints[i + 2]);
//            }
//        }
//        gl.glEnd();

        gl.glMapGrid1f(bezierPartitions, 0f, 1f);
        gl.glEvalMesh1(gl.GL_POINT, 0, bezierPartitions);

        gl.glEnable(gl.GL_LINE_SMOOTH);
        gl.glLineWidth(lineWidth);
        
        gl.glBegin(gl.GL_LINE_STRIP);
        {
            for (float v = 0; v <= 1; v += (1.0f / lineSteps)) {
                gl.glEvalCoord1f(v);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();

        labelRect.getCenter().set(
            ctrlPoints[3], ctrlPoints[4], ctrlPoints[5]
            );
        labelRect.getSize().set(0.1f, 0.1f, 0.1f);
        labelRect.draw(gl);
    }


//    @Override public void draw(GL2 gl) {
//        gl.glColor3f(1f, 1f, 1f);
//        gl.glPushMatrix();
//        transform(gl);
//
//        // Draw ctrlPoints.
//        gl.glBegin(GL.GL_LINES);
//        {
//            gl.glVertex3f(aRect.getCenter().x(), aRect.getCenter().y(), aRect.getCenter().z());
//            gl.glVertex3f(bRect.getCenter().x(), bRect.getCenter().y(), bRect.getCenter().z());
//        }
//        gl.glEnd();
//        gl.glPopMatrix();
//    }
}
