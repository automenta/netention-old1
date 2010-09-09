/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.math.linalg.Vec2f;
import automenta.spacegraph.math.linalg.Vec3f;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Curve;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.shape.WideIcon;
import automenta.spacegraph.ui.Window;
import com.sun.opengl.util.awt.TextRenderer;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.math.Vector;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class GraphSpace<N, E extends DirectedEdge<N>> extends DefaultSurface {

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
    private TextRenderer tr;
    private GraphDrawer<Graph<N,E>,N> layout;
    
    protected final Map<E, Curve> edgeLines = new HashMap<E, Curve>();
    
    public GraphSpace(Graph<N, E> graph, GraphDrawer<Graph<N, E>, N> initialLayout) {
        super();

        setLayout(initialLayout);
        
        this.sg = graph;       
        
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
                Logger.getLogger(GraphSpace.class.toString()).severe("could not find boxes for edge: " + e);
                continue;
            }

            final WideIcon curveLabel = new WideIcon(e.toString(), getColor(null), getColor(null));

            Curve c = new Curve(aBox, bBox, 2) {

                @Override
                public void draw(GL2 gl) {
                    super.draw(gl);
                    curveLabel.move(
                        ctrlPoints[3], ctrlPoints[4], ctrlPoints[5]);
                    curveLabel.scale(0.1f, 0.1f, 0.1f);
                    //curveLabel.draw(gl);
                }
            };

            edgeLines.put(e, c);
            add(c);
        }


    }
    
    public void setLayout(GraphDrawer<Graph<N,E>,N> newLayout) {
        this.layout = newLayout;        
    }

    public Rect newNodeRect(N n) {
        WideIcon box = new WideIcon(n.toString(), getColor(n), getColor(n));
        //Window box = new Window();
        return box;
    }

    public Vec4f getColor(N n) {
        return new Vec4f(Color.getHSBColor((float) Math.random(), 0.75f, 1.0f));
    }

//    @Override
//    public void mousePressed(MouseEvent e) {
//        super.mousePressed(e);
//        downPixel = new Vec2f(e.getX(), e.getY());
//        downPointPos = new Vec3f(targetPos);
//        downPointTarget = new Vec3f(targetTarget);
//    }

//    @Override
//    public void mouseWheelMoved(MouseWheelEvent e) {
//        super.mouseWheelMoved(e);
//
//        Vec3f delta = new Vec3f(0, 0, e.getWheelRotation() * 4.0f);
//        targetPos.add(delta);
//        targetTarget.add(delta);
//    }

//    @Override
//    public void mouseDragged(MouseEvent e) {
//        super.mouseDragged(e);
//        xAng = e.getX();
//        yAng = e.getY();
//
//        Vec3f delta = new Vec3f(-(xAng - downPixel.x()), yAng - downPixel.y(), 0);
//        delta.scale(0.01f);
//
//        targetPos.set(downPointPos);
//        targetTarget.set(downPointTarget);
//        targetPos.add(delta);
//        targetTarget.add(delta);
//    }

    @Override
    protected void updateSpace(GL2 gl) {
        float m = 3.0f;
        
//        for (N s : sg.getNodes()) {
//            Vector v = layout.getCoordinates().get(s);
//            Rect r = boxes.get(s);
//            float x = r.getCenter().x() , y = r.getCenter().y(), z = r.getCenter().z();
//            if (v.getCoordinate(1) != x*m) {
//                System.out.println("discrep: " + v.getCoordinate(1) + " : " + x);
//            }
//            v.setCoordinate(x/m, 1);
//            if (v.getDimensions() > 1)
//                v.setCoordinate(y/m, 2);
//            if (v.getDimensions() > 2)
//                v.setCoordinate(z/m, 3);
//        }
               
        if (layout.isAlignable())
            layout.align();
        
        for (N s : sg.getNodes()) {
            Vector v = layout.getCoordinates().get(s);
            Rect b = boxes.get(s);
            if (v.getDimensions() == 1) {
                float x = (float) (v.getCoordinate(1) * m);
                b.moveTo(0, x, 0);
            } else if (v.getDimensions() == 2) {
                float x = (float) (v.getCoordinate(1) * m);
                float y = (float) (v.getCoordinate(2) * m);
                b.moveTo(x, y, 0);
            } else if (v.getDimensions() == 3) {
                float x = (float) (v.getCoordinate(1) * m);
                float y = (float) (v.getCoordinate(2) * m);
                float z = (float) (v.getCoordinate(3) * m);
                b.moveTo(x, y, z);
            }
            updateRect(s, b);
        }

        super.updateSpace(gl);
//        getCamera().camPos.lerp(targetPos, 0.95f);
//        getCamera().camTarget.lerp(targetTarget, 0.95f);
    }

    protected void updateRect(N s, Rect r) {
        r.scale(0.5f, 0.5f, 0.5f);
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
