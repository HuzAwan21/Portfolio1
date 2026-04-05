package routing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Factory {

	/**
	 * Create a graph from the description in a .nae file.
	 *
	 * @param fileName
	 *            A path to an NAE file.
	 *
	 * @return The graph as described in the .nae file.
	 *
	 * @throws IOException
	 *             If an Input/Output error occurs.
	 */
	public static Graph createGraphFromMap(String fileName) throws IOException {
		GraphFactory graph = new GraphFactory();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals("N")) {
                    long nodeId = Long.parseLong(parts[1]);
                    double latitude = Double.parseDouble(parts[2]);
                    double longitude = Double.parseDouble(parts[3]);
                    NodeFactory node = new NodeFactory(nodeId, latitude, longitude);
                    graph.addNode(node);
                } else if (parts[0].equals("E")) {
                    long startNodeId = Long.parseLong(parts[1]);
                    long destinationNodeId = Long.parseLong(parts[2]);
                    boolean forwardPassableByCar = parts[3].equals("1");
                    boolean backwardPassableByCar = parts[4].equals("1");
                    boolean forwardPassableByBike = parts[5].equals("1");
                    boolean backwardPassableByBike = parts[6].equals("1");
                    boolean forwardWalkable = parts[7].equals("1");
                    boolean backwardWalkable = parts[8].equals("1");

                    Node startNode = graph.getNode(startNodeId);
                    Node destinationNode = graph.getNode(destinationNodeId);

                    if (startNode != null && destinationNode != null) {
                        EdgeFactory toEdge = new EdgeFactory(
                            startNode, destinationNode,
                            forwardPassableByCar, backwardPassableByCar,
                            forwardPassableByBike, backwardPassableByBike,
                            forwardWalkable, backwardWalkable
                        );
                        EdgeFactory fromEdge = new EdgeFactory(
                            destinationNode, startNode,
                            backwardPassableByCar, forwardPassableByCar,
                            backwardPassableByBike, forwardPassableByBike,
                            backwardWalkable, forwardWalkable
                        );
                        startNode.addEdge(toEdge);
                        destinationNode.addEdge(fromEdge);
                        graph.addEdge(toEdge);
                        graph.addEdge(fromEdge);
                    }
                }
            }
        }

        return graph;
    }
	/**
	 * Return a node finder algorithm for the graph g. The graph argument allows
	 * the node finder to build internal data structures.
	 *
	 * @param g
	 *            The graph the nodes are looked up in.
	 * @return A node finder algorithm for that graph.
	 */
	
	public static NodeFinder createNodeFinder(Graph g) {
        int cellsize = 100; 
        return new NodeFinderGrid(g, cellsize);
	}

	/**
	 * == BONUS ==
	 *
	 * Compute the overlay graph (or junction graph).
	 *
	 * Note: This is part of a bonus exercise, not of the regular project.
	 *
	 * @return The overlay graph for the given graph g.
	 */
	public static Graph createOverlayGraph(Graph g) {
		// TODO: Implement me.
		return null;
	}

	/**
	 * Return a routing algorithm for the graph g. This allows to inspect the
	 * graph and choose from different routing strategies if appropriate.
	 *
	 * @param g
	 *            The graph the routing is performed on.
	 * @return A routing algorithm suitable for that graph.
	 */
	public static RoutingAlgorithm createRoutingAlgorithm(Graph g) {

		return new DijkstraRoutingAlgorithm();
	}

}
