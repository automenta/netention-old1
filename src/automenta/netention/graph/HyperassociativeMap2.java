package automenta.netention.graph;

import com.syncleus.dann.graph.*;
import java.util.*;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.math.Vector;

public class HyperassociativeMap2<G extends Graph<N, ?>, N> implements GraphDrawer<G,N> {
	private final G graph;
	private final int dimensions;
	private final Map<N, Vector> coordinates = Collections.synchronizedMap(new HashMap<N, Vector>());
	private final static Random RANDOM = new Random();

	private double equilibriumDistance;
	private double learningRate;

	public HyperassociativeMap2(G graph, int dimensions, double learningRate, double equilibriumDistance)
	{
		this.graph = graph;
		this.dimensions = dimensions;
        this.learningRate = learningRate;
        this.equilibriumDistance = equilibriumDistance;

		//refresh all nodes
		for(N node : this.graph.getNodes())
			this.coordinates.put(node, randomCoordinates(this.dimensions));
	}


	public G getGraph()
	{
		return this.graph;
	}

	public boolean isAlignable()
	{
		return false;
	}

	public boolean isAligned()
	{
		return false;
	}

	public void align()
	{
		//refresh all nodes
		final Map<N, Vector> newCoordinates = new HashMap<N,Vector>();
		for(N node : this.graph.getNodes())
		{
			if( this.coordinates.containsKey(node) )
				newCoordinates.put(node, this.coordinates.get(node));
			else
				newCoordinates.put(node, randomCoordinates(this.dimensions));
		}
		this.coordinates.clear();
		this.coordinates.putAll(newCoordinates);

        //TODO javolution ConcurrentContext this
        Vector center = new Vector(dimensions);
		for(N node : this.graph.getNodes()) {
			Vector newPoint = align(node);
            for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
					center = center.setCoordinate(center.getCoordinate(dimensionIndex) + newPoint.getCoordinate(dimensionIndex), dimensionIndex);
        }

		//divide each coordinate of the sum of all the points by the number of
		//nodes in order to calulate the average point, or center of all the
		//points
		for(int dimensionIndex = 1; dimensionIndex <= this.dimensions; dimensionIndex++)
			center = center.setCoordinate(center.getCoordinate(dimensionIndex)/((double)this.graph.getNodes().size()),dimensionIndex);

		this.recenterNodes(center);
	}

	public int getDimensions()
	{
		return this.dimensions;
	}

	public Map<N, Vector> getCoordinates()
	{
		return Collections.unmodifiableMap(this.coordinates);
	}

	private void recenterNodes(final Vector center)
	{
		for(N node : this.graph.getNodes())
			this.coordinates.put(node, this.coordinates.get(node).calculateRelativeTo(center));
	}

	Set<N> getNeighbors(N nodeToQuery)
	{
		return new HashSet<N>(this.graph.getAdjacentNodes(nodeToQuery));
	}

	final private Vector align(N nodeToAlign)
	{
        //calculate equilibrium with neighbors
		final Vector location = this.coordinates.get(nodeToAlign);
		final Set<N> neighbors = this.getNeighbors(nodeToAlign);
        final Set<Edge<N>> edges = new HashSet(graph.getAdjacentEdges(nodeToAlign));

        Vector compositeVector = new Vector(location.getDimensions());
        //for(N neighbor : neighbors)
        for (Edge<N> edge : edges)
        {
            for (N neighbor : edge.getNodes() ) {
                if (neighbor!=nodeToAlign) {

                    double eq = getEqulibriumDistance(nodeToAlign, edge);
                    
                    Vector neighborVector = this.coordinates.get(neighbor).calculateRelativeTo(location);
                    if (Math.abs(neighborVector.getDistance()) > eq)
                    {
                        double newDistance = Math.pow(Math.abs(neighborVector.getDistance()) - eq, 2.0);
                        if(Math.abs(newDistance) > Math.abs(Math.abs(neighborVector.getDistance()) - eq))
                            newDistance = Math.abs(Math.abs(neighborVector.getDistance()) - eq);
                        neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
                    }
                    else
                    {
                        double newDistance = -1.0 * atanh((eq - Math.abs(neighborVector.getDistance())) / eq);
                        if( Math.abs(newDistance) > Math.abs(eq - Math.abs(neighborVector.getDistance())))
                            newDistance = -1.0 * (eq - Math.abs(neighborVector.getDistance()));
                        neighborVector = neighborVector.setDistance(Math.signum(neighborVector.getDistance()) * newDistance);
                    }

                    compositeVector = compositeVector.add(neighborVector);
                }
            }
            
        }

        //calculate repulsion with all non-neighbors
        for (N node : this.graph.getNodes())
            if ((neighbors.contains(node) == false)&&(node != nodeToAlign)&&(this.graph.getAdjacentNodes(node).contains(nodeToAlign) == false) )
            {
                Vector nodeVector = this.coordinates.get(node).calculateRelativeTo(location);
				double newDistance = -1.0/Math.pow(nodeVector.getDistance(), 2.0);
				if(Math.abs(newDistance) > Math.abs(equilibriumDistance))
					newDistance = -1.0 * this.equilibriumDistance;
                nodeVector = nodeVector.setDistance(newDistance);

                compositeVector = compositeVector.add(nodeVector);
            }

        compositeVector = compositeVector.setDistance(compositeVector.getDistance() * learningRate);

		Vector newLocation = location.add(compositeVector);
        this.coordinates.put(nodeToAlign, newLocation);
		return newLocation;
	}

    /**
     * can be overridden to weight different edge types
     * @param node
     * @param edge
     * @return
     */
    public double getEqulibriumDistance(N node, Edge<N> edge) {
        return equilibriumDistance;
    }

	/**
	 * Obtains a Vector with random coordinates for the specified number of
	 * dimensions.
	 *
	 *
	 * @param dimensions Number of dimensions for the random Vector
	 * @return New random Vector
	 * @since 1.0
	 */
    public static Vector randomCoordinates(int dimensions)
    {
        double[] randomCoords = new double[dimensions];
        for (int randomCoordsIndex = 0; randomCoordsIndex < dimensions; randomCoordsIndex++)
            randomCoords[randomCoordsIndex] = (RANDOM.nextDouble() * 2.0) - 1.0;

        return new Vector(randomCoords);
    }

    private static double atanh(double value)
    {
        return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
    }


}
