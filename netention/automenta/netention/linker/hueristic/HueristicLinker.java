package automenta.netention.linker.hueristic;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.graph.Pair;
import automenta.netention.graph.ValueEdge;

import automenta.netention.linker.Linker;
import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections15.IteratorUtils;

abstract public class HueristicLinker implements Linker {

    private Self self;

    public HueristicLinker(Self self) {
        super();
        this.self = self;
    }

    public BidirectedGraph<Node, ValueEdge<Node, Link>> run() {
        return run(IteratorUtils.toList(self.iterateNodes()));
    }

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> run(final Collection<Node> details) {
        final MutableBidirectedGraph<Node, ValueEdge<Node, Link>> graph = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>();
        
        final Map<Pair<Node>, Link> similarity = new HashMap();
        
        for (final Node nd : details) {
            for (final Node nn : details) {
                if (nd == nn) {
                    continue;
                }

                if (!(nd instanceof Detail) && (nn instanceof Detail)) {
                    continue;
                }

                final Detail d = (Detail) nd;
                final Detail n = (Detail) nn;

                Link simLink = similarity.get(new Pair<Node>(d, n));
                                
                if (simLink == null)  {
                    simLink = compareSimilarity(d, n);
                    if (simLink!=null) {
                        similarity.put(new Pair<Node>(d, n), simLink);
                        similarity.put(new Pair<Node>(n, d), simLink);
                    }
                }

                if (d.getMode() == Mode.Real) {
                    if (n.getMode() == Mode.Imaginary) {

                        Link link = compareSatisfying(d, n);
                        if (link != null) {
                            addEdge(graph, new ValueEdge<Node, Link>(link, d, n));
                        }
                    }
                }

            }
        }
        return graph;

    }

    public void addEdge(MutableBidirectedGraph<Node, ValueEdge<Node, Link>> graph, ValueEdge<Node, Link> e) {
        graph.add(e.getSourceNode());
        graph.add(e.getDestinationNode());
        graph.add(e);
    }
    
    abstract public Link compareSatisfying(Detail real, Detail imaginary);
    
    //Transitive function
    abstract public Link compareSimilarity(Detail a, Detail b);

    public Self getSelf() {
        return self;
    }
}
