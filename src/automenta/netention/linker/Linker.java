package automenta.netention.linker;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import java.util.Collection;


/** a weaver is a process that semantically links stories in real-time */
public interface Linker {

    public SimpleDynamicDirectedGraph<Node, Link> run(Collection<Detail> details);

}
