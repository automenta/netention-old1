package automenta.netention.linker.hueristic;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Self;
import java.util.HashSet;
import java.util.Set;

import automenta.netention.linker.Linker;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.util.Collection;

abstract public class HueristicLinker implements Linker {

    private Set<Detail> nodes = new HashSet();

    public HueristicLinker() {
        super();
    }

    public DirectedGraph<Detail,Link> run(Self self) {
        return run(self.getDetails().values());
    }
    
    public DirectedGraph<Detail,Link> run(Collection<Detail> details) {
        DirectedSparseMultigraph<Detail,Link> graph = new DirectedSparseMultigraph();
        for (Detail d : details) {
            for (Detail n : details) {
                if (d == n) {
                    continue;
                }

                if (d.getMode() == Mode.Real) {
                    if (n.getMode() == Mode.Imaginary) {

                        Link link = compareSatisfying(n, d);
                        if (link != null) {
                            if (link.getStrength() > getStrengthThreshold()) {
                                graph.addVertex(d);
                                graph.addVertex(n);
                                graph.addEdge(link, d, n);
                            }
                        }
                    }
                }
            }
        }
        return graph;

    }

    private double getStrengthThreshold() {
        return 0.0;
    }

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
