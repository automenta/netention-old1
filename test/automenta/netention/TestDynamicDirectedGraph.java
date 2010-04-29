/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import automenta.netention.graph.DynamicDirectedGraph;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.graph.ValueDirectedEdge;
import junit.framework.TestCase;

/**
 *
 * @author seh
 */
public class TestDynamicDirectedGraph extends TestCase {
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

    public void testSimpleDynamicDirectedGraph() {
        SimpleDynamicDirectedGraph<Integer,String> ddg = new SimpleDynamicDirectedGraph();
        ddg.addNode(0);
        ddg.addNode(1);
        ValueDirectedEdge<Integer,String> e = ddg.addEdge("a", 0, 1);
        
        {
            assertEquals(2, ddg.getOrder());
            assertTrue(ddg.getAdjacentNodes(0).contains(1));
            assertTrue(ddg.getAdjacentNodes(1).contains(0));
            assertTrue(ddg.containsVertex(1));
            assertTrue(ddg.containsVertex(0));
        }

        ddg.removeEdge(e);

        {
            assertTrue(!ddg.getAdjacentNodes(0).contains(1));
        }

        ddg.removeVertex(0);

        {
            assertEquals(1, ddg.getOrder());
        }

    }

    public void testDynamicDirectedGraph() {
        DynamicDirectedGraph ddg = new DynamicDirectedGraph();
        ddg.addNode("x");
        ddg.addNode("y");

        ValueDirectedEdge e = new ValueDirectedEdge("z", "x", "y");
        ddg.addEdge(e);

        {
            assertEquals(2, ddg.getOrder());
            assertTrue(ddg.getAdjacentNodes("x").contains("y"));
            assertTrue(ddg.getAdjacentNodes("y").contains("x"));
            assertTrue(ddg.containsVertex("x"));
            assertTrue(ddg.containsVertex("y"));
        }

        ddg.removeEdge(e);
        
        {
            assertTrue(!ddg.getAdjacentNodes("x").contains("y"));
        }

        ddg.removeVertex("x");

        {
            assertEquals(1, ddg.getOrder());
        }
        
    }
}
