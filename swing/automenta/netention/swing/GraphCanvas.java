/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.shape.Curve;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.shape.WideIcon;
import com.sun.opengl.util.awt.TextRenderer;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.hyperassociativemap.HyperassociativeMap;
import com.syncleus.dann.math.Vector;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class GraphCanvas<N, E extends DirectedEdge<N>> extends DefaultSurface {

    private float textScaleFactor;
    float xAng = 0;
    float yAng = 0;
    private final Graph<N, E> sg;
    private final Map<N, Rect> boxes = new HashMap();
    private Vec3f targetPos = new Vec3f(0, 0, 10);
    private Vec3f targetTarget = new Vec3f(0, 0, 0);
    private Vec3f downPointPos;
    private Vec3f downPointTarget;
    private Vec2f downPixel;
    public final HyperassociativeMap<Graph<N, E>, N> hmap;
    private TextRenderer tr;

    protected final Map<E, Curve> edgeLines = new HashMap<E, Curve>();
    
    public GraphCanvas(Graph<N, E> graph, int dimensions) {
        super();

        this.sg = graph;

        hmap = new HyperassociativeMap(sg, dimensions, 0.01, false);

        //tr = TextRect.newTextRenderer(new Font("Arial", Font.PLAIN, 72));

        for (N s : sg.getNodes()) {
            Rect box = newNodeRect(s);
            boxes.put(s, box);
            this.add(box);
        }
        for (E e : sg.getEdges()) {
            Rect aBox = boxes.get(e.getSourceNode());
            Rect bBox = boxes.get(e.getDestinationNode());
            if ((aBox == null) || (bBox == null)) {
                Logger.getLogger(GraphCanvas.class.toString()).severe("could not find boxes for edge: " + e);
                continue;
            }

            final WideIcon curveLabel = new WideIcon(e.toString(), getColor(null), getColor(null));

            Curve c = new Curve(aBox, bBox, 2) {

                @Override
                public void draw(GL2 gl) {
                    super.draw(gl);
                    curveLabel.getCenter().set(
                        ctrlPoints[3], ctrlPoints[4], ctrlPoints[5]);
                    curveLabel.getSize().set(0.1f, 0.1f, 0.1f);
                    //curveLabel.draw(gl);
                }
            };

            edgeLines.put(e, c);
            add(c);
        }


    }

    public Rect newNodeRect(N n) {
        WideIcon box = new WideIcon(n.toString(), getColor(n), getColor(n));
        return box;
    }

    public Vec3f getColor(N n) {
        return new Vec3f().fromColor(Color.getHSBColor((float) Math.random(), 0.75f, 1.0f));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //hmap.randomizeAllCoordinates();
            randomizeHyperassociativeMap(hmap, 4.0);
        }
    }

    public void randomizeHyperassociativeMap(HyperassociativeMap h, double f) {
        List<N> l = new LinkedList<N>(h.getCoordinates().keySet());
        for (N n : l) {
            //synchronized (h.getCoordinates()) {
            Vector v = (Vector)h.getCoordinates().get(n);
            for (int i = 1; i <= v.getDimensions(); i++) {
                double c = (Math.random() * 2.0 - 1.0) * f;
                v.setCoordinate(c, i);
            }
            //put(n, h.randomCoordinates(h.getDimensions()));
            //}
        }
    }

    public void mouseMoved(MouseEvent e)    {
        super.mouseMoved(e);
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

        Vec3f delta = new Vec3f(0, 0, e.getWheelRotation() * 4.0f);
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
            Rect b = boxes.get(s);
            if (v.getDimensions() == 1) {
                float x = (float) (v.getCoordinate(1) * m);
                b.getCenter().set(0, x, 0);
            } else if (v.getDimensions() == 2) {
                float x = (float) (v.getCoordinate(1) * m);
                float y = (float) (v.getCoordinate(2) * m);
                b.getCenter().set(x, y, 0);
            } else if (v.getDimensions() == 3) {
                float x = (float) (v.getCoordinate(1) * m);
                float y = (float) (v.getCoordinate(2) * m);
                float z = (float) (v.getCoordinate(3) * m);
                b.getCenter().set(x, y, z);
            }
            updateRect(s, b);
        }

        getCamera().camPos.lerp(targetPos, 0.95f);
        getCamera().camTarget.lerp(targetTarget, 0.95f);
    }

    protected void updateRect(N s, Rect r) {
        r.getSize().set(0.5f, 0.5f, 0.5f);
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

//    public static void main(String[] args) {
//
//        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());
//
//        MemorySelf self = new MemorySelf("me", "Me");
//        new SeedSelfBuilder().build(self);
//
//        //self.addPlugin(new Twitter());
//
//        self.updateLinks(null);
//
//        MutableBidirectedGraph<Node, ValueEdge<Node, Link>> target = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>(self.getGraph());
//        //MetadataGrapher.run(self, target, true, true, true, true);
//        MetadataGrapher.run(self, target, true, true, true, true);
//
//        new SGWindow("DemoSGCanvas", new GraphCanvas(target, 3));
//    }
    
}
