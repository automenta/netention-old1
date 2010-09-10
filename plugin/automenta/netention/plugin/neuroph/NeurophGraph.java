/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.neuroph;

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.xml.GraphXml;
import com.syncleus.dann.xml.Namer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 *
 * @author seh
 */
public class NeurophGraph implements BidirectedGraph<Neuron, ConnectionEdge> {
    private final NeuralNetwork net;

    public NeurophGraph(NeuralNetwork net) {
        super();
        this.net = net;
    }

    public Set<ConnectionEdge> getInEdges(Neuron n) {
        Set<ConnectionEdge> inEdges = new HashSet();
        for (Connection c : n.getInputConnections()) {
            inEdges.add(new ConnectionEdge(c, c.getConnectedNeuron(), n));
        }
        return Collections.unmodifiableSet(inEdges);
    }

    public int getOutdegree(Neuron n) {
        return n.getOutConnections().size();
    }

    public int getIndegree(Neuron n) {
        return n.getInputConnections().size();
    }

    public boolean isPolytree() {
        return false;
    }

    public Set<Neuron> getNodes() {
        Set<Neuron> nodes = new HashSet();
        nodes.addAll(net.getInputNeurons());
        nodes.addAll(net.getOutputNeurons());
        for (Layer l : net.getLayers())
            nodes.addAll(l.getNeurons());
        return Collections.unmodifiableSet(nodes);
    }

    public static void addNeuronEdges(Neuron n, Set<ConnectionEdge> edges) {
        for (Connection i : n.getInputConnections()) {
            edges.add(new ConnectionEdge(i, i.getConnectedNeuron(), n));
        }
        for (Connection i : n.getOutConnections()) {
            edges.add(new ConnectionEdge(i, n, i.getConnectedNeuron()));
        }
    }

    public Set<ConnectionEdge> getEdges() {
        Set<ConnectionEdge> edges = new HashSet();
        for (Neuron n : net.getInputNeurons())
            addNeuronEdges(n, edges);
        for (Neuron n : net.getOutputNeurons())
            addNeuronEdges(n, edges);
        for (Layer l : net.getLayers()) {
            for (Neuron n : l.getNeurons())
                addNeuronEdges(n, edges);
        }
        
        return Collections.unmodifiableSet(edges);
    }

    public List<Neuron> getAdjacentNodes(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<ConnectionEdge> getAdjacentEdges(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Neuron> getTraversableNodes(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<ConnectionEdge> getTraversableEdges(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDegree(Neuron n) {
        return n.getInputConnections().size() + n.getOutConnections().size();
    }

    public boolean isStronglyConnected(Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isStronglyConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWeaklyConnected(Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWeaklyConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Graph<Neuron, ConnectionEdge>> getMaximallyConnectedComponents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isMaximalSubgraph(Graph<Neuron, ConnectionEdge> graph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Set<Neuron> set, Set<ConnectionEdge> set1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Set<ConnectionEdge> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(ConnectionEdge e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Set<Neuron> set, Set<ConnectionEdge> set1, Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Set<ConnectionEdge> set, Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(Neuron n, Neuron n1, Neuron n2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCut(ConnectionEdge e, Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNodeConnectivity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEdgeConnectivity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNodeConnectivity(Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEdgeConnectivity(Neuron n, Neuron n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isComplete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getOrder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCycleCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPancyclic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isUnicyclic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAcyclic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getGirth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCircumference() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSpanningTree(Graph<Neuron, ConnectionEdge> graph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isForest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSubGraph(Graph<Neuron, ConnectionEdge> graph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMinimumDegree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRegularDegree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isMultiple(ConnectionEdge e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMultiplicity(ConnectionEdge e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMultiplicity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isMultigraph() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIsomorphic(Graph<Neuron, ConnectionEdge> graph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isHomomorphic(Graph<Neuron, ConnectionEdge> graph) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRegular() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSimple() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isKnot(Set<Neuron> set, Set<ConnectionEdge> set1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isKnot(Set<Neuron> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneAdd(ConnectionEdge e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneAdd(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneAdd(Set<Neuron> set, Set<ConnectionEdge> set1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneRemove(ConnectionEdge e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneRemove(Neuron n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> cloneRemove(Set<Neuron> set, Set<ConnectionEdge> set1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Graph<Neuron, ConnectionEdge> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isContextEnabled() {
        return false;
    }

    @Override
    public GraphXml toXml(Namer<Object> namer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GraphXml toXml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void toXml(GraphXml t, Namer<Object> namer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    


}
