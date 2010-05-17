/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import automenta.netention.graph.DynamicDirectedGraph;
import automenta.netention.graph.ValueEdge;
import com.syncleus.dann.graph.ImmutableDirectedEdge;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import junit.framework.TestCase;

/**
 *
 * @author seh
 */
public class TestDynamicDirectedGraph extends TestCase {
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

    public void testSimpleDynamicDirectedGraph() {
        MutableDirectedAdjacencyGraph<Integer, ImmutableDirectedEdge<Integer>> ddg = new MutableDirectedAdjacencyGraph<Integer, ImmutableDirectedEdge<Integer>>();

        
        ddg.add(0);
        ddg.add(1);

        
        ddg.add(new ImmutableDirectedEdge<Integer>(0, 1));
        {
            assertEquals(2, ddg.getOrder());
            assertTrue(ddg.getAdjacentNodes(0).contains(1));
            assertTrue(ddg.getAdjacentNodes(1).contains(0));
            //assertTrue(ddg.containsVertex(1));
            //assertTrue(ddg.containsVertex(0));
        }

//        ddg.remove(e);
//        {
//            assertTrue(!ddg.getAdjacentNodes(0).contains(1));
//        }

        ddg.remove(0);
        {
            assertEquals(1, ddg.getOrder());
        }

    }

//    public void testDynamicDirectedGraph() {
//        DynamicDirectedGraph ddg = new DynamicDirectedGraph();
//        ddg.addNode("x");
//        ddg.addNode("y");
//
//        ValueDirectedEdge e = new ValueDirectedEdge("z", "x", "y");
//        ddg.addEdge(e);
//        {
//            assertEquals(2, ddg.getOrder());
//            assertTrue(ddg.getAdjacentNodes("x").contains("y"));
//            assertTrue(ddg.getAdjacentNodes("y").contains("x"));
//            assertTrue(ddg.containsVertex("x"));
//            assertTrue(ddg.containsVertex("y"));
//        }
//
//        ddg.removeEdge(e);
//        {
//            assertTrue(!ddg.getAdjacentNodes("x").contains("y"));
//        }
//
//        ddg.removeVertex("x");
//        {
//            assertEquals(1, ddg.getOrder());
//        }
//
//    }
    
}
