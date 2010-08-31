/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph;

import automenta.spacegraph.control.Camera;
import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Repeat;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.demo.jogl.FPSCounter;
import automenta.spacegraph.demo.jogl.SystemTime;
import automenta.spacegraph.demo.jogl.Time;
import com.sun.opengl.util.BufferUtil;
import java.awt.event.MouseEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 *
 * @author seh
 */
abstract public class Surface extends SG {

    private Vec3f background = new Vec3f(0, 0, 0);
    float nearF = 3f;
    float farF = 50.0f;
    private GLU glu = new GLU();
    protected final Time time = new SystemTime();
    private FPSCounter fps;
    private Camera camera = new Camera();
    private Pointer pointer = new Pointer();
    float focus = 45.0f;
    boolean showFPS = true;
    Space2D space;

    public Surface() {
        super();
    }

    public Surface(Space2D initialSpace) {
        this();
        setSpace(space);
    }

    public void setSpace(Space2D space) {
        this.space = space;
    }

    public double getDT() {
        return time.deltaT();
    }

    public double getT() {
        return time.time();
    }

    public void init(GLAutoDrawable g) {
        GL2 gl = g.getGL().getGL2();

        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glDepthRange(0, 1);

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
        glu.gluPerspective(focus, (float) w / (float) h, nearF, farF);

        gl.glViewport(0, 0, w, h);
    }
    
    int[] viewport = new int[4];

    public void display(GLAutoDrawable g) {
        g.getContext().makeCurrent();

        GL2 gl = g.getGL().getGL2();

        GLU glu = new GLU();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(background.x(), background.y(), background.z(), 1f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        glu.gluLookAt(getCamera().camPos.x(), getCamera().camPos.y(), getCamera().camPos.z(),
                getCamera().camTarget.x(), getCamera().camTarget.y(), getCamera().camTarget.z(),
                getCamera().camUp.x(), getCamera().camUp.y(), getCamera().camUp.z());

        drawSpace(gl);


        if (showFPS) {
            fps.draw();
        }


        gl.glGetIntegerv(gl.GL_VIEWPORT, viewport, 0);

        //FOREACH pointer
        {

            //visible rectangle determination    
            float visR = (float) ((Math.sin(Math.toRadians(focus) / 2.0) * getCamera().camPos.z()) / Math.sin(Math.PI / 2.0 - Math.toRadians(focus) / 2.0));
            
            //System.out.println("focus: " + visR + " @ " + getCamera().camPos.z());
            
            float cx = getCamera().camPos.x();
            float cy = getCamera().camPos.y();

            float w = viewport[2];
            float h = viewport[3];
            
            float aspect = (((float)h) / ((float)w));
            
            //TODO handle case where h > w           
            float visY = visR*2.0f;
            float visX = visR / aspect*2.0f;

            float vx = visX/2.0f;
            float vy = visY/2.0f;
            float tiltAngle = (float)Math.atan2(getCamera().camUp.y(), getCamera().camUp.x());
            
            Vec2f dwY = new Vec2f(
             visR * (float)Math.cos(tiltAngle),
             visR * (float)Math.sin(tiltAngle));
            Vec2f dwX = new Vec2f(
             visR * (float)Math.cos(tiltAngle - Math.PI/2.0),
             visR * (float)Math.sin(tiltAngle - Math.PI/2.0));
             dwY.normalize();
             dwX.normalize();
            
            Vec2f dw = new Vec2f(dwY);
            dw.add(dwX);
            dw.normalize();
            
            //System.out.println("tiltAngle = " + tiltAngle + " dw=" + dw.x() + " , dw=" + dw.y() + ":::" + " dwx=" + dwX.x() + " , dwy=" + dwX.y()+ ":::" + " dwx=" + dwY.x() + " , dwy=" + dwY.y());
            
//            float blX = cx - vx;
//            float blY = cy - vy;
//
//            float urX = cx + vx;
//            float urY = cy + vy;

            float pixX = (pointer.pixel.x());
            float pixY = (viewport[3] - pointer.pixel.y());
            
            float mpctX = (pixX / viewport[2])-0.5f;
            float mpctY = (pixY / viewport[3])-0.5f;
            
            
//            float dx = (blX + mpctX * visX) - cy;
//            float dy = (blY + mpctY * visY) - cx;
//
//            System.out.println(mpctX + " % " + mpctY);
//            System.out.println("  " + dx + " % " + dy);
//            System.out.println("  " + cx + " , " + cy + " in " + blX + "," + blY  + " .. " + urX + " ," + urY);
//
//            //TODO rotate based on tilt
//            float tiltAngle = (float)Math.atan2(getCamera().camUp.y(), getCamera().camUp.x());
//            float dwx = dx * (float) Math.cos(tiltAngle - (float) Math.PI / 2.0f) - dy * (float) Math.sin(tiltAngle - (float) Math.PI / 2.0f);
//            float dwy = dx * (float) Math.sin(tiltAngle - (float) Math.PI / 2.0f) + dy * (float) Math.cos(tiltAngle - (float) Math.PI / 2.0f);
//                    
//            float wx = cx + dx;
//            float wy = cy + dy;
            
            float wx = cx + (float)(mpctX * visX*2.0)/2.0f;
            float wy = cy + (float)(mpctY * visY*2.0)/2.0f;
            
            pointer.world.set(wx, wy, 0);
            
            handleTouch(pointer);
            
            time.update();
        }

        gl.glFlush();
        g.getContext().release();

    }
    
    protected void handleTouch(Pointer p) {
    }

    protected void updateSpace(GL2 gl) {
        //TODO calculate real inter-frame dt
        double dt = 0.01;

        for (Repeat r : repeats) {
            r.update(dt, t);
        }

        t += dt;
    }

    protected void drawSpace(GL2 gl) {
        updateSpace(gl);

        if (space != null) {
            space.draw(gl);
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
