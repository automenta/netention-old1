/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph;

import automenta.spacegraph.gleem.linalg.Vec2f;
import automenta.spacegraph.gleem.linalg.Vec3f;
import automenta.spacegraph.demo.jogl.FPSCounter;
import automenta.spacegraph.demo.jogl.SystemTime;
import automenta.spacegraph.demo.jogl.Time;
import automenta.spacegraph.shape.Drawable;
import java.awt.event.MouseEvent;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author seh
 */
public class SGCanvas extends SG {

    public static class Camera {
        public final Vec3f camPos = new Vec3f(0, 0, 10);
        public final Vec3f camTarget = new Vec3f(0, 0, 0);
        public final Vec3f camUp = new Vec3f(0, 1, 0);
    }
    public static class Pointer {
        public final Vec2f pixel = new Vec2f(0,0);
    }

    float nearF = 5f;
    float farF = 15.0f;
    
    protected List<Drawable> drawables = new LinkedList();
    private GLU glu = new GLU();
    protected final Time time = new SystemTime();
    private FPSCounter fps;
    private Camera camera = new Camera();
    private Pointer pointer = new Pointer();

    public SGCanvas() {
        super();

    }

    public double getDT() {
        return time.deltaT();
    }
    public double getT() {
        return time.time();
    }


    public void init(GLAutoDrawable g) {

        GL2 gl = g.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        //Lighting, see nehe.gamedev.net/data/lessons/lesson.asp?lesson=07
        {
            FloatBuffer lightAmbient = FloatBuffer.wrap(new float[] { 0.5f, 0.5f, 0.5f, 1.0f } );
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient);				// Setup The Ambient Light

            FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[] { 1f, 1f, 1f, 1f } );
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse);				// Setup The Diffuse Light

            FloatBuffer lightPosition = FloatBuffer.wrap(new float[] { 0f, 0f, 20f, 1f } ); //Leave the last number at 1.0f. This tells OpenGL the designated coordinates are the position of the light source. More about this in a later tutorial.
            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition);			// Position The Light

            gl.glEnable(GL2.GL_LIGHT1);							// Enable Light One
            gl.glEnable(GL2.GL_LIGHTING);
        }

        fps = new FPSCounter(g, 36);

        ((SystemTime) time).rebase();
        //    gl.setSwapInterval(0);

    }

    public void dispose(GLAutoDrawable g) {
    }

    public void display(GLAutoDrawable g) {
        GL2 gl = g.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        glu.gluLookAt(getCamera().camPos.x(), getCamera().camPos.y(), getCamera().camPos.z(),
            getCamera().camTarget.x(), getCamera().camTarget.y(), getCamera().camTarget.z(),
            getCamera().camUp.x(), getCamera().camUp.y(), getCamera().camUp.z());

        //draw space
        drawSpace(gl);

        fps.draw();

        time.update();

    }
    
    protected void updateSpace(GL2 gl) {

    }

    protected void drawSpace(GL2 gl) {
        updateSpace(gl);
        
        for (Drawable d : drawables) {
            d.draw(gl);
        }
   
    }

    public void reshape(GLAutoDrawable g, int x, int y, int w, int h) {
        GL2 gl = g.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(15, (float) w / (float) h, nearF, farF);

    }

    public Camera getCamera() {
        return camera;
    }

    public void removeAll() {
        drawables.clear();
    }

    public <D extends Drawable> D add(D d) {
        drawables.add(d);
        return d;
    }
    public boolean remove (Drawable d) {
        return drawables.add(d);
    }

    protected void updateMouseLocation(float nx, float ny) {
        pointer.pixel.set(nx, ny);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        updateMouseLocation((float)e.getPoint().getX(), (float)e.getPoint().getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        updateMouseLocation((float)e.getPoint().getX(), (float)e.getPoint().getY());
    }

    public Pointer getPointer() {
        return pointer;
    }

    

}
