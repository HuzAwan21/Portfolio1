package routing;

import java.util.*;

public class DijkstraRoutingAlgorithm implements RoutingAlgorithm {

    @Override
    public Route computeRoute(Graph graph, List<Node> waypoints, TravelType travelType) throws NoSuchRouteException {
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("Waypoints list cannot be null or empty");
        }

        List<RouteLeg> legs = new ArrayList<>();


        Node start = waypoints.get(0);
        for (int i = 1; i < waypoints.size(); i++) {
            Node end = waypoints.get(i);
            RouteLeg leg = computeRouteLeg(graph, start, end, travelType);
            legs.add(leg);
            start = end;
        }

        Route route = new RouteImpl(legs);


        return route;
    }

    @Override
    public RouteLeg computeRouteLeg(Graph graph, long startNodeId, long endNodeId, TravelType travelType) throws NoSuchRouteException {
        Node startNode = graph.getNode(startNodeId);
        Node endNode = graph.getNode(endNodeId);
        if (startNode == null || endNode == null) {
            throw new NoSuchRouteException("Start or end node not found");
        }
        return computeRouteLeg(graph, startNode, endNode, travelType);
    }

    @Override
    public RouteLeg computeRouteLeg(Graph graph, Node startNode, Node endNode, TravelType travelType) throws NoSuchRouteException {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Set<Node> visited = new HashSet<>();

        for (Node node : graph) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(startNode, 0.0);

        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (current.equals(endNode)) {
                return reconstructRouteLeg(startNode, endNode, previousNodes, travelType);
            }

            for (Edge edge : current) {
                if (!edge.allowsTravelType(travelType, Direction.FORWARD)) {
                    continue;
                }
                Node neighbor = edge.getEnd();
                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDistance = distances.get(current) + edge.getLength();

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNodes.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }

        throw new NoSuchRouteException("No route found from startNode to endNode");
    }

    private RouteLeg reconstructRouteLeg(Node startNode, Node endNode, Map<Node, Node> previousNodes, TravelType travelType) throws NoSuchRouteException {
        LinkedList<Node> path = new LinkedList<>();
        Node current = endNode;

        while (current != null) {
            path.addFirst(current);
            current = previousNodes.get(current);
        }

        if (!path.isEmpty() && path.getFirst().equals(startNode)) {
            return new RouteLegImpl(path, travelType);
        } else {
            throw new NoSuchRouteException("No route found from startNode to endNode");
        }
    }

    @Override
    public boolean isBidirectional() {
        return false;
    }

}
