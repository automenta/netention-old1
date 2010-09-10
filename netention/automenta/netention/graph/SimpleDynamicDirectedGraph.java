package automenta.netention.graph;

/** BidirectedGraph that can be edited dynamically: adding and removing both nodes and edges.
    Contains code adapted from JUNG2's DirectedSparseMultigraph and its ancestry.  */
public class SimpleDynamicDirectedGraph<N, E> /*extends AbstractBidirectedGraph<N, ValueDirectedEdge<N,E>> implements Serializable*/ {
//    //Original dANN fields:
//    //	final private HashSet<N> nodes;
//    //	final private HashSet<E> edges;
//    //	final private Map<N, Set<E>> neighborEdges = new HashMap<N, Set<E>>();
//    //	final private Map<N, List<N>> neighborNodes = new HashMap<N, List<N>>();
//
//    //JUNG2's DirectedSparseMultigraph fields, for comparison:
//    protected final Map<N, Pair<Set<ValueDirectedEdge<N,E>>>> vertices; // Map of vertices to Pair of adjacency sets {incoming, outgoing}
//    //protected Map<E, Pair<N>> edges;            // Map of edges to incident vertex pairs
//    protected final Set<ValueDirectedEdge<N,E>> edges;
//
//
//    public SimpleDynamicDirectedGraph() {
//        super();
//        vertices = new HashMap();
//        edges = new HashSet();
//    }
//
//    public SimpleDynamicDirectedGraph(SimpleDynamicDirectedGraph<N, E> copyFrom) {
//        super();
//        this.vertices = new HashMap(copyFrom.vertices);
//        this.edges = new HashSet(copyFrom.edges);
//    }
//
//    public synchronized void clear() {
//        vertices.clear();
//        edges.clear();
//    }
//
//    public boolean addNode(N vertex) {
//        if (vertex == null) {
//            throw new IllegalArgumentException("vertex may not be null");
//        }
//        if (!containsVertex(vertex)) {
//            vertices.put(vertex, new Pair<Set<ValueDirectedEdge<N,E>>>(new HashSet<ValueDirectedEdge<N,E>>(), new HashSet<ValueDirectedEdge<N,E>>()));
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean removeVertex(N vertex) {
//        if (!containsVertex(vertex)) {
//            return false;
//        }
//
//        // copy to avoid concurrent modification in removeEdge
//        Set<ValueDirectedEdge<N,E>> incident = new HashSet<ValueDirectedEdge<N,E>>(getIncoming_internal(vertex));
//        incident.addAll(getOutgoing_internal(vertex));
//
//        for (ValueDirectedEdge<N,E> edge : incident) {
//            removeEdge(edge);
//        }
//
//        vertices.remove(vertex);
//
//        return true;
//    }
//
//
//    public ValueDirectedEdge<N,E> addEdge(E edgeValue, N source, N dest) {
//        ValueDirectedEdge<N,E> edge = new ValueDirectedEdge(edgeValue, source, dest);
//        final Pair<N> endpoints = new Pair<N>(edge.getSourceNode(), edge.getDestinationNode());
//        final Pair<N> new_endpoints = getValidatedEndpoints(edge, endpoints);
//        if (new_endpoints == null) {
//            return null;
//        }
//
//        edges.add(edge);
//
//        if (!containsVertex(source)) {
//            this.addNode(source);
//        }
//
//        if (!containsVertex(dest)) {
//            this.addNode(dest);
//        }
//
//        getIncoming_internal(dest).add(edge);
//        getOutgoing_internal(source).add(edge);
//
//        return edge;
//    }
//
//    public boolean removeEdge(ValueDirectedEdge<N,E> edge) {
//        if (!containsEdge(edge)) {
//            return false;
//        }
//
//        Pair<N> endpoints = this.getEndpoints(edge);
//        N source = endpoints.getFirst();
//        N dest = endpoints.getSecond();
//
//        // remove edge from incident vertices' adjacency sets
//        getOutgoing_internal(source).remove(edge);
//        getIncoming_internal(dest).remove(edge);
//
//        edges.remove(edge);
//        return true;
//    }
//
//    public boolean containsVertex(N vertex) {
//    	return vertices.keySet().contains(vertex);
//    }
//
//    public boolean containsEdge(ValueDirectedEdge<N,E> edge) {
//    	return edges.contains(edge);
//    }
//
//
//    protected Pair<N> getValidatedEndpoints(ValueDirectedEdge<N,E> edge, Pair<? extends N> endpoints)
//    {
//        if (edge == null)
//            throw new IllegalArgumentException("input edge may not be null");
//
//        if (endpoints == null)
//            throw new IllegalArgumentException("endpoints may not be null");
//
//        Pair<N> new_endpoints = new Pair<N>(endpoints.getFirst(), endpoints.getSecond());
//        if (containsEdge(edge))
//        {
//            Pair<N> existing_endpoints = getEndpoints(edge);
//            if (!existing_endpoints.equals(new_endpoints)) {
//                throw new IllegalArgumentException("edge " + edge +
//                        " already exists in this graph with endpoints " + existing_endpoints +
//                        " and cannot be added with endpoints " + endpoints);
//            } else {
//                return null;
//            }
//        }
//        return new_endpoints;
//    }
//
//    public Pair<N> getEndpoints(ValueDirectedEdge<N,E> edge) {
//        if (edges.contains(edge)) {
//            return new Pair(edge.getSourceNode(), edge.getDestinationNode());
//        }
//        return null;
//    }
//
//    protected Set<ValueDirectedEdge<N,E>> getIncoming_internal(N vertex)
//    {
//        return vertices.get(vertex).getFirst();
//    }
//
//    protected Collection<ValueDirectedEdge<N,E>> getOutgoing_internal(N vertex)
//    {
//        return vertices.get(vertex).getSecond();
//    }
//
//    public Set<N> getNodes() {
//        return Collections.unmodifiableSet(this.vertices.keySet());
//    }
//
//    @Override
//    public Set<ValueDirectedEdge<N,E>> getEdges() {
//        return Collections.unmodifiableSet(this.edges);
//    }
//
//    public Set<ValueDirectedEdge<N,E>> getAdjacentEdges(N node) {
//        //return Collections.unmodifiableSet(this.neighborEdges.get(node));
//        if (!containsVertex(node))
//            return null;
//
//        Set<ValueDirectedEdge<N,E>> incident = new HashSet();
//        incident.addAll(getIncoming_internal(node));
//        incident.addAll(getOutgoing_internal(node));
//        return Collections.unmodifiableSet(incident);
//    }
//
//    public Set<ValueDirectedEdge<N,E>> getTraversableEdges(N node) {
//        final Set<ValueDirectedEdge<N,E>> traversableEdges = new HashSet();
//        for (ValueDirectedEdge<N,E> edge : edges) {
//            if (edge.getSourceNode() == node) {
//                traversableEdges.add(edge);
//            }
//        }
//        return Collections.unmodifiableSet(traversableEdges);
//    }
//
//    public Set<ValueDirectedEdge<N,E>> getOutEdges(N node) {
//        return this.getTraversableEdges(node);
//    }
//
//    public Set<ValueDirectedEdge<N,E>> getInEdges(N node) {
//        return getIncoming_internal(node);
//    }
//
//    public int getIndegree(N node) {
//        return this.getInEdges(node).size();
//    }
//
//    public int getOutdegree(N node) {
//        return this.getOutEdges(node).size();
//    }
//
//    public List<N> getAdjacentNodes(N vertex) {
//        if (!containsVertex(vertex))
//            return null;
//
//        List<N> neighbors = new LinkedList<N>();
//        for (ValueDirectedEdge<N,E> edge : getIncoming_internal(vertex))
//            neighbors.add(this.getSource(edge));
//        for (ValueDirectedEdge<N,E> edge : getOutgoing_internal(vertex))
//            neighbors.add(this.getDest(edge));
//        return Collections.unmodifiableList(neighbors);
//    }
//
//    public List<N> getTraversableNodes(N node) {
//        Set<ValueDirectedEdge<N,E>> traversableEdges = this.getTraversableEdges(node);
//        List<N> traversableNeighbors = new ArrayList<N>();
//        for (ValueDirectedEdge<N,E> traversableEdge : traversableEdges) {
//            traversableNeighbors.add(traversableEdge.getDestinationNode());
//        }
//        return Collections.unmodifiableList(traversableNeighbors);
//    }
//
//    public N getSource(ValueDirectedEdge<N,E> edge) {
//        if (!containsEdge(edge))
//            return null;
//        return this.getEndpoints(edge).getFirst();
//    }
//
//    public N getDest(ValueDirectedEdge<N,E> edge) {
//        if (!containsEdge(edge))
//            return null;
//        return this.getEndpoints(edge).getSecond();
//    }
//
//
}
