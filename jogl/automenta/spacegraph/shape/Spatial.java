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
class Spatial {
    protected Vec3f center;
    protected Vec3f size;
    protected Vec3f rotation;
    //private final Mat4f transform = new Mat4f();

    public Spatial(Vec3f center, Vec3f size, Vec3f rotation) {
        super();
        this.center = center;
        this.size = size;
        this.rotation = rotation;
    }

    public Vec3f getCenter() {
        return center;
    }

    public Vec3f getSize() {
        return size;
    }

//    public Rotf getRotation() {
//        return rotation;
//    }

    public Vec3f getRotation() {
        return rotation;
    }
    
    protected void transform(GL2 gl) {
//        transform.makeIdent();
//        transform.setScale(getSize());
//        transform.setTranslation(getCenter());
//        transform.setRotation(getRotation());
//        gl.glLoadMatrixf(transform.data, 0);

        gl.glScalef(getSize().x(), getSize().y(), getSize().z());
        gl.glTranslatef(getCenter().x(), getCenter().y(), getCenter().z());

        gl.glRotatef(getRotation().x(), 1, 0, 0);
        gl.glRotatef(getRotation().y(), 0, 1, 0);
        gl.glRotatef(getRotation().z(), 0, 0, 1);

    }

}
