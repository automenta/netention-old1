/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.graph;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.MutableAdjacencyGraph;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author me
 */
public class NotifyingDirectedGraph<N, E extends Edge<N>> extends MutableAdjacencyGraph<N, E> {

    
    public static interface GraphListener<N, E extends Edge<N>> {
        public void onNodeAdded(N n);
        public void onNodeRemoved(N n);
        public void onEdgeAdded(E e);
        public void onEdgeRemoved(E e);
    }
    
    private List<GraphListener<N, E>> listeners = new LinkedList();

    public NotifyingDirectedGraph() {
        super();
    }

    public NotifyingDirectedGraph(Graph<N, E> graph) {
        super(graph);
    }
    
    /** adds a listener, and then calls onNodeAdded and onEdgeAdded for all existing nodes|edges */
    public boolean addListener(GraphListener<N,E> l) {
        boolean b = listeners.add(l);
        if (b) {
            for (N n : getNodes()) {
                l.onNodeAdded(n);
            }
            for (E e : getEdges()) {
                l.onEdgeAdded(e);
            }
            return true;
        }
        return false;
    }
    
    public boolean removeListener(GraphListener<N,E> l) {
        return listeners.remove(l);
    }

    @Override
    public boolean add(E newEdge) {
        boolean b = super.add(newEdge);
        if (b) {
            for (GraphListener<N,E> g : listeners) {
                g.onEdgeAdded(newEdge);
            }                
            return true;
        }
        return false;
    }

    @Override
    public boolean add(N newNode) {
        boolean b = super.add(newNode);
        if (b) {
            for (GraphListener<N,E> g : listeners) {
                g.onNodeAdded(newNode);
            }                
            
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(E edgeToRemove) {
        boolean b = super.remove(edgeToRemove);
        if (b) {
            for (GraphListener<N,E> g : listeners) {
                g.onEdgeRemoved(edgeToRemove);
            }                            
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(N nodeToRemove) {
        boolean b = super.remove(nodeToRemove);
        if (b) {
            for (GraphListener<N,E> g : listeners) {
                g.onNodeRemoved(nodeToRemove);
            }                                        
            return true;
        }
        return false;
    }
    
    
    

}
