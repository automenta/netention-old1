/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.demo;

import automenta.netention.graph.HyperassociativeMap3;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.spacegraph.SGCanvas;
import automenta.spacegraph.SGWindow;
import automenta.spacegraph.gleem.linalg.Vec2f;
import automenta.spacegraph.gleem.linalg.Vec3f;
import automenta.spacegraph.shape.TextRect;
import com.syncleus.dann.graph.AbstractBidirectedGraph;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.SimpleDirectedEdge;
import com.syncleus.dann.math.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.media.opengl.GL2;
import javolution.context.ConcurrentContext;

/**
 *
 * @author seh
 */
public class DemoHyperassoc<N, E extends SimpleDirectedEdge<N>> extends SGCanvas {

    private float textScaleFactor;
    float xAng = 0;
    float yAng = 0;
    private final AbstractBidirectedGraph<N, E> sg;
    private final Map<N, TextRect> boxes = new HashMap();
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private Vec3f targetPos = new Vec3f(0,0,10);
    private Vec3f targetTarget = new Vec3f(0,0,0);
    private Vec3f downPointPos;
    private Vec3f downPointTarget;
    private Vec2f downPixel;
    private final HyperassociativeMap3<Graph<N, E>, N> hmap;

    public DemoHyperassoc(AbstractBidirectedGraph<N, E> graph, int dimensions) {
        super();

        this.sg = graph;
        
        hmap = new HyperassociativeMap3(sg, dimensions, 0.004, 1.0, 2, 4);

        for (N s : sg.getNodes()) {
            TextRect box = new TextRect(s.toString());
            boxes.put(s, box);
            add(box);
            System.out.println(" +" + s);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            hmap.randomizeAllCoordinates();
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        downPixel = new Vec2f(e.getX(), e.getY());
        downPointPos = new Vec3f(targetPos);
        downPointTarget = new Vec3f(targetTarget);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);

        Vec3f delta = new Vec3f(0, 0, e.getWheelRotation()*0.5f);
        targetPos.add(delta);
        targetTarget.add(delta);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        xAng = e.getX();
        yAng = e.getY();

        Vec3f delta = new Vec3f(-(xAng - downPixel.x()), yAng - downPixel.y(), 0);
        delta.scale(0.01f);
        
        targetPos.set(downPointPos);
        targetTarget.set(downPointTarget);
        targetPos.add(delta);
        targetTarget.add(delta);
    }

    @Override
    protected void updateSpace(GL2 gl) {
        float m = 3.0f;

        hmap.align();
        
        for (N s : sg.getNodes()) {
            Vector v = hmap.getCoordinates().get(s);
            TextRect b = boxes.get(s);
            if (v.getDimensions() == 1) {
                float x = (float)(v.getCoordinate(1)*m);
                b.getCenter().set(0, x, 0);
            }
            else if (v.getDimensions() == 2) {
                float x = (float)(v.getCoordinate(1)*m);
                float y = (float)(v.getCoordinate(2)*m);
                b.getCenter().set(x, y, 0);
            }
            else if (v.getDimensions() == 3) {
                float x = (float)(v.getCoordinate(1)*m);
                float y = (float)(v.getCoordinate(2)*m);
                float z = (float)(v.getCoordinate(3)*m);
                b.getCenter().set(x, y, z);
            }
            b.getSize().set(0.25f, 0.25f, 0.25f);
        }

        getCamera().camPos.lerp(targetPos, 0.95f);
        getCamera().camTarget.lerp(targetTarget, 0.95f);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
//        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//            targetPos.add(new Vec3f(-0.1f, 0, 0));
//            targetTarget.add(new Vec3f(-0.1f, 0, 0));
//        }
//        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//            targetPos.add(new Vec3f(0.1f, 0, 0));
//            targetTarget.add(new Vec3f(0.1f, 0, 0));
//        }
    }

    public static void main(String[] args) {

        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());
        
        //StringGraph s = new StringGraph();
        SimpleDynamicDirectedGraph<String,String> s = new SimpleDynamicDirectedGraph();
        s.addNode("a");
        s.addNode("b");
        s.addNode("c");
        s.addNode("d");
        s.addNode("e");

        s.addEdge("ab", "a", "b");
        s.addEdge("cd", "c", "d");
        s.addEdge("cd", "a", "c");
        s.addEdge("cd", "a", "d");
        s.addEdge("cd", "a", "e");
        
        new SGWindow("DemoSGCanvas", new DemoHyperassoc(s, 2));
    }
}
