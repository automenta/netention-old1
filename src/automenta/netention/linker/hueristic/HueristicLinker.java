package automenta.netention.linker.hueristic;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.graph.ValueEdge;
import java.util.HashSet;
import java.util.Set;

import automenta.netention.linker.Linker;
import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.util.Collection;
import org.apache.commons.collections15.IteratorUtils;

abstract public class HueristicLinker implements Linker {

    private Set<Detail> nodes = new HashSet();

    public HueristicLinker() {
        super();
    }

    public BidirectedGraph<Node,ValueEdge<Node, Link>> run(Self self) {
        return run(IteratorUtils.toList(self.iterateDetails()));
    }
    
    public MutableBidirectedGraph<Node,ValueEdge<Node, Link>> run(Collection<Node> details) {
        MutableBidirectedGraph<Node,ValueEdge<Node, Link>> graph = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>();
        for (Node nd : details) {
            for (Node nn : details) {
                if (nd == nn) {
                    continue;
                }

                if (!(nd instanceof Detail) && (nn instanceof Detail))
                    continue;

                Detail d = (Detail)nd;
                Detail n = (Detail)nn;

                if (d.getMode() == Mode.Real) {
                    if (n.getMode() == Mode.Imaginary) {

                        Link link = compareSatisfying(n, d);
                        if (link != null) {
                            //if (link.getStrength() > getStrengthThreshold()) {
                                graph.add(d);
                                graph.add(n);
                                graph.add(new ValueEdge(link, d, n));
                            //}
                        }
                    }
                }
            }
        }
        return graph;

    }

//    private double getStrengthThreshold() {
//        return 0.0;
//    }

    abstract public Link compareSatisfying(Detail real, Detail imaginary);

    protected void addNode(Detail n) {
        synchronized (nodes) {
            nodes.add(n);
        }
    }

    protected boolean containsNode(Detail n) {
        boolean b;
        synchronized (nodes) {
            b = nodes.contains(n);
        }
        return b;
    }

}
