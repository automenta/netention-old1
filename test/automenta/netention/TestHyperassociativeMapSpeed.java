/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import automenta.netention.graph.HyperassociativeMap3;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import com.syncleus.dann.graph.drawing.hyperassociativemap.HyperassociativeMap;
import javolution.context.ConcurrentContext;

/**
 * Compares dANN HyperassociativeMap and HyperassociativeMap3
 */
public class TestHyperassociativeMapSpeed {

    public static SimpleDynamicDirectedGraph<Integer, Integer> newRandomGraph(int numVertices, int numEdges) {
        SimpleDynamicDirectedGraph<Integer,Integer> g = new SimpleDynamicDirectedGraph<Integer, Integer>();
        for (int i = 0; i < numVertices; i++) {
            g.addNode(i);
        }
        for (int e = 0; e < numEdges; e++) {

            int a = (int)(Math.random() * numVertices);
            int b = -1;

            do {
                b = (int)(Math.random() * numVertices);
            } while (b!=a);

            g.addEdge(e + numVertices + 1, a, b);
        }
        System.out.println("Nodes: " + g.getNodes().size());
        System.out.println("Edges: " + g.getEdges().size());
        return g;
    }


    public static long now() {
        return System.nanoTime();
    }

    public static void main(String[] args) {
        int dimensions = 3;

        int numVertices = 100;
        int numEdges = 100;

        int iterations = 700;
        
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());
        //ConcurrentContext.setConcurrency(1);

        SimpleDynamicDirectedGraph<Integer,Integer> sg = newRandomGraph(numVertices, numEdges);


        System.gc();
        {
            System.out.println("HyperassociativeMap3 (SeH Javolution): ");
            HyperassociativeMap3 hmap = new HyperassociativeMap3(sg, dimensions, 0.004, 1.0, 2, 4);
            long h3Start = now();
            for (int i = 0; i < iterations; i++)
                hmap.alignJavolution();
            long h3Ends = now();

            double time = (h3Ends - h3Start) / 1e9;
            System.out.println(time + "s total; " + time/((float)iterations) + "s per iterations");
            hmap.dispose();
        }

        System.gc();
        {
            System.out.println("HyperassociativeMap3 (SeH Threadpool): ");
            HyperassociativeMap3 hmap = new HyperassociativeMap3(sg, dimensions, 0.004, 1.0, 2, 4);
            long h3Start = now();
            for (int i = 0; i < iterations; i++)
                hmap.align();
            long h3Ends = now();

            double time = (h3Ends - h3Start) / 1e9;
            System.out.println(time + "s total; " + time/((float)iterations) + "s per iterations");
            hmap.dispose();
        }

        System.gc();
        {
            System.out.println("HyperassociativeMap (original): ");
            HyperassociativeMap hmap = new HyperassociativeMap(sg, dimensions);
            long hStart = now();
            for (int i = 0; i < iterations; i++)
                hmap.align();
            long hEnds = now();

            double time = (hEnds - hStart) / 1e9;
            System.out.println(time + "s total; " + time/((float)iterations) + "s per iterations");

        }


        System.exit(0);

    }

}
