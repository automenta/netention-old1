/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;

/**
 *
 * @author seh
 */
public class UnionGraph<N, E extends DirectedEdge<N>> extends MutableDirectedAdjacencyGraph<N,E> {

    public UnionGraph(BidirectedGraph<N,E>... graphs) {
        super();

        for (BidirectedGraph<N, E> g : graphs)
            includeGraph(g);
    }

    public void includeGraph(BidirectedGraph<N, E> graph) {
        for (N n : graph.getNodes()) {
            add(n);
        }
        for (E e : graph.getEdges()) {
            add(e);
        }
    }



}
