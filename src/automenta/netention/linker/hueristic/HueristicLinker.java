package automenta.netention.linker.hueristic;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import java.util.HashSet;
import java.util.Set;

import automenta.netention.linker.Linker;
import java.util.Collection;
import org.apache.commons.collections15.IteratorUtils;

abstract public class HueristicLinker implements Linker {

    private Set<Detail> nodes = new HashSet();

    public HueristicLinker() {
        super();
    }

    public SimpleDynamicDirectedGraph<Node,Link> run(Self self) {
        return run(IteratorUtils.toList(self.iterateDetails()));
    }
    
    public SimpleDynamicDirectedGraph<Node,Link> run(Collection<Detail> details) {
        SimpleDynamicDirectedGraph<Node,Link> graph = new SimpleDynamicDirectedGraph();
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
                                graph.addNode(d);
                                graph.addNode(n);
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
