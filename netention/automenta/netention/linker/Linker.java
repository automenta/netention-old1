package automenta.netention.linker;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.ValueEdge;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import java.util.Collection;


/** a weaver is a process that semantically links stories in real-time */
public interface Linker {

    public MutableBidirectedGraph<Node, ValueEdge<Node, Link>> run(Collection<Node> details);

}
