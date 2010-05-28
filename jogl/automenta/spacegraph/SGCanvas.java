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
import com.sun.opengl.util.BufferUtil;
import java.awt.event.MouseEvent;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author seh
 */
public class SGCanvas extends SG {

    private Vec3f background = new Vec3f(0, 0, 0);

    public static class Camera {

        public final Vec3f camPos = new Vec3f(0, 0, 10);
        public final Vec3f camTarget = new Vec3f(0, 0, 0);
        public final Vec3f camUp = new Vec3f(0, 1, 0);
    }

    public static class Pointer {

        public final Vec2f pixel = new Vec2f(0, 0);
    }
    float nearF = 5f;
    float farF = 100.0f;
    protected List<Drawable> drawables = new CopyOnWriteArrayList<Drawable>();
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
//            FloatBuffer lightAmbient = FloatBuffer.wrap(new float[] { 0.5f, 0.5f, 0.5f, 1.0f } );
//            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient);				// Setup The Ambient Light
//
//            FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[] { 1f, 1f, 1f, 1f } );
//            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse);				// Setup The Diffuse Light
//
//            FloatBuffer lightPosition = FloatBuffer.wrap(new float[] { 0f, 0f, 20f, 1f } ); //Leave the last number at 1.0f. This tells OpenGL the designated coordinates are the position of the light source. More about this in a later tutorial.
//            gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition);			// Position The Light
//            gl.glEnable(GL2.GL_LIGHT1);							// Enable Light One
//            gl.glEnable(GL2.GL_LIGHTING);
        }

        fps = new FPSCounter(g, 36);

        ((SystemTime) time).rebase();
        //    gl.setSwapInterval(0);

    }

    public void dispose(GLAutoDrawable g) {
    }

    public void reshape(GLAutoDrawable g, int x, int y, int w, int h) {
        GL2 gl = g.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(15, (float) w / (float) h, nearF, farF);
    }

    public static enum RenderMode {

        Draw, Touch
    }

    public void render(GLAutoDrawable g, RenderMode mode) {
        GL2 gl = (GL2) g.getGL();
        GLU glu = new GLU();

            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            gl.glClearColor(background.x(), background.y(), background.z(), 1f);

            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();

            glu.gluLookAt(getCamera().camPos.x(), getCamera().camPos.y(), getCamera().camPos.z(),
                getCamera().camTarget.x(), getCamera().camTarget.y(), getCamera().camTarget.z(),
                getCamera().camUp.x(), getCamera().camUp.y(), getCamera().camUp.z());


        if (mode == RenderMode.Touch) {
            double x = (double) pointer.pixel.x(), y = (double) pointer.pixel.y();

                 System.out.println(x + " " + y);

                int buffsize = 512;
                int[] viewPort = new int[4];
                IntBuffer selectBuffer = BufferUtil.newIntBuffer(buffsize);
                int hits = 0;
                gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
                gl.glSelectBuffer(buffsize, selectBuffer);
                gl.glRenderMode(GL2.GL_SELECT);
                gl.glInitNames();
                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glPushMatrix();
                gl.glLoadIdentity();
                glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
                glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);

                drawSpace(gl);

                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glPopMatrix();
                gl.glFlush();
                hits = gl.glRenderMode(GL2.GL_RENDER);
                processHits(hits, selectBuffer);
        }
        else {
            drawSpace(gl);

            fps.draw();

            time.update();

        }

   }

    public void display(GLAutoDrawable g) {
        g.getContext().makeCurrent();

        //render(g, RenderMode.Touch);
        render(g, RenderMode.Draw);

        g.getContext().release();

    }

    protected void updateSpace(GL2 gl) {
    }

    protected void drawSpace(GL2 gl) {
        updateSpace(gl);

        int id = 0;
        for (Drawable d : drawables) {
            gl.glPushName(id++);            
            d.draw(gl);
            gl.glPopName();
        }

    }

    public void processHits(int hits, IntBuffer buffer) {
        System.out.println("---------------------------------");
        System.out.println(" HITS: " + hits);
        int offset = 0;
        int names;
        float z1, z2;
        for (int i = 0; i < hits; i++) {
            System.out.println("- - - - - - - - - - - -");
            System.out.println(" hit: " + (i + 1));
            names = buffer.get(offset);
            offset++;
            z1 = (float) buffer.get(offset) / 0x7fffffff;
            offset++;
            z2 = (float) buffer.get(offset) / 0x7fffffff;
            offset++;
            System.out.println(" number of names: " + names);
            System.out.println(" z1: " + z1);
            System.out.println(" z2: " + z2);
            System.out.println(" names: ");

            for (int j = 0; j < names; j++) {
                System.out.print("       " + buffer.get(offset));
                if (j == (names - 1)) {
                    System.out.println("<-");
                } else {
                    System.out.println();
                }
                offset++;
            }
            System.out.println("- - - - - - - - - - - -");
        }
        System.out.println("---------------------------------");
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

    public boolean remove(Drawable d) {
        return drawables.add(d);
    }

    protected void updateMouseLocation(float nx, float ny) {
        pointer.pixel.set(nx, ny);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        updateMouseLocation((float) e.getPoint().getX(), (float) e.getPoint().getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        updateMouseLocation((float) e.getPoint().getX(), (float) e.getPoint().getY());
    }

    public Pointer getPointer() {
        return pointer;
    }

    public void setBackground(Vec3f c) {
        this.background = c;
    }
}
