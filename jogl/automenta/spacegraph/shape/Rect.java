/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.shape;

import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Touchable;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec4f;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class Rect extends Spatial implements Drawable, Touchable {

    Vec4f backgroundColor = new Vec4f(0.5f, 0.5f, 0.5f, 1.0f);

    boolean filled = true;

    public Rect() {
        super();
    }
    
    public Rect tilt(float newTilt) {
        rotation.setZ(newTilt);
        return this;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public Rect color(float r, float g, float b) {
        return color(r, g, b, 1.0f);
    }

    public Rect color(float r, float g, float b, float a) {
        return color(new Vec4f(r, g, b, a));
    }

    public Rect color(Vec4f c) {
        setBackgroundColor(c);
        return this;
    }

    public void setBackgroundColor(Vec4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vec4f getBackgroundColor() {
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
            gl.glColor4f(backgroundColor.x(), backgroundColor.y(), backgroundColor.z(), backgroundColor.w());
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

    public Rect scale(float sx, float sy) {
        scale(sx, sy, 1.0f);
        return this;
    }

    @Override
    public boolean isTouchable() {
        return true;
    }

    public static boolean CircleIntersection(float cx, float cy, float radius, float px, float py) {
        double d = (px - cx)*(px - cx) + (py - cy)*(py - cy);
        return (d < radius*radius);
    }
    
    @Override
    public boolean intersects(Vec2f p) {
        float radius = (float)Math.max(size.x(), size.y());
        if (CircleIntersection(this.center.x(), this.center.y(), radius, p.x(), p.y())) {
            return true;
        }
        
        return false;
    }

    @Override
    public void onTouchChange(Pointer pointer, Vec2f p, boolean touched) {
        
    }
    
    
}
