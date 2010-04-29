package automenta.netention.graph;

import com.syncleus.dann.graph.SimpleDirectedEdge;

public class ValueDirectedEdge<N,V> extends SimpleDirectedEdge<N> {

    public final V value;

    public ValueDirectedEdge(V value, N source, N target) {
        super(source, target);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return value.equals(((ValueDirectedEdge) obj).value);
        }
        return false;
    }

    public V getValue() {
        return value;
    }

    
}
