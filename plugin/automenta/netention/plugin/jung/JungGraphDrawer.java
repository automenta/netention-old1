/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.jung;

import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.math.Vector;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author me
 */
public class JungGraphDrawer<G extends Graph<N, ?>, N> implements GraphDrawer<G, N> {
    private final Layout<Object, Object> layout;

    public final G graph;
    public final Map<N, Vector> coordinates = new WeakHashMap();
    
    //multiplyer to adjust between floats and ints
    float magLevel = 256.0f;
    
    public JungGraphDrawer(final G graph, Layout<Object, Object> layout, float w, float h) {
        super();
        this.layout = layout;
        this.graph = graph;
        layout.initialize();
        layout.setSize(new Dimension((int)(magLevel * w), (int)(magLevel * h)));
    }

    
    
    @Override
    public G getGraph() {
        return graph;
    }

    @Override
    public boolean isAlignable() {
        if (layout instanceof IterativeContext) {
            IterativeContext ic = (IterativeContext)layout;
            return !ic.done();
        }
        return true;
    }

    @Override
    public boolean isAligned() {
        if (layout instanceof IterativeContext) {
            IterativeContext ic = (IterativeContext)layout;
            return ic.done();
        }
        return false;
    }

    @Override
    public void align() {
        if (layout instanceof IterativeContext) {
            IterativeContext ic = (IterativeContext)layout;
            ic.step();
        }
        

        synchronized (coordinates) {
            if (layout instanceof Transformer) {
                for (N n : getGraph().getNodes()) {
                    Transformer<N,Point2D> t = (Transformer<N,Point2D>)layout;
                    Point2D p = t.transform(n);
                    
                    Vector v = new Vector(((float)p.getX() - layout.getSize().width/2)/magLevel, ((float)p.getY() - layout.getSize().height/2 )/magLevel);
                    coordinates.put(n, v);
                }
            }
        }
    
    }

    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public Map<N, Vector> getCoordinates() {
        return coordinates;
    }

    @Override
    public void reset() {
        layout.initialize();
    }

}
