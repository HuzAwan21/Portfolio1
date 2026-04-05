package routing;

import java.util.*;

public class GraphFactory implements Graph {

    private Map<Long, Node> nodes;
    private List<Edge> edges;

    public GraphFactory() {
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    @Override
    public Node getNode(long id) {
    if (id < 0 || nodes.get(id)==null) {
        return null;
    }
    return nodes.get(id);
    }

    @Override
    public Coordinate getNWCoordinate() {
        double maxLat = Double.MIN_VALUE;
        double maxLon = Double.MIN_VALUE;
        for (Node node : nodes.values()) {
            Coordinate coord = node.getCoordinate();
            if (coord.getLatitude() > maxLat) {
                maxLat = coord.getLatitude();
            }
            if (coord.getLongitude() > maxLon) {
                maxLon = coord.getLongitude();
            }
        }
        return new Coordinate(maxLat, maxLon);
    }

    @Override
    public Coordinate getSECoordinate() {
        double minLat = Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE;
        for (Node node : nodes.values()) {
            Coordinate coord = node.getCoordinate();
            if (coord.getLatitude() < minLat) {
                minLat = coord.getLatitude();
            }
            if (coord.getLongitude() < minLon) {
                minLon = coord.getLongitude();
            }
        }
        return new Coordinate(minLat, minLon);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public int numNodes() {
        return nodes.size();
    }


    @Override
    public int removeIsolatedNodes() {
        int removedCount = 0;
    
    Iterator<Map.Entry<Long, Node>> iterator = nodes.entrySet().iterator();
    for (;iterator.hasNext();) {
        Map.Entry<Long, Node> entry = iterator.next();
        Node node = entry.getValue();
        
        if (node.numEdges() == 0) {
            iterator.remove(); 
            removedCount++;

        }
        
    }
    
    return removedCount;
    }

    @Override
    public int removeUntraversableEdges(RoutingAlgorithm ra, TravelType tt) {
        int removedCount = 0;
        boolean isBidirectional = ra.isBidirectional();
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            boolean removeEdge;
            if (isBidirectional){
                removeEdge = !edge.allowsTravelType(tt, Direction.FORWARD) 
                          && !edge.allowsTravelType(tt, Direction.BACKWARD);
            } else { removeEdge = !edge.allowsTravelType(tt, Direction.FORWARD);}
                    
            if (removeEdge) {
                iterator.remove();

                Node StartNode = edge.getStart();
                Node EndNode = edge.getEnd();
                removeEdgeFromNode(StartNode, edge);
                removeEdgeFromNode(EndNode, edge);

                removedCount++;
            }
        }
        return removedCount;
    }

    private void removeEdgeFromNode(Node node, Edge edge) {
        for (int i = 0; i < node.numEdges(); i++) {
            if (node.getEdge(i).equals(edge)) {
                node.removeEdge(i);
                break;
            }
        }
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public boolean isOverlayGraph() {
        return false;
    }

    @Override
    public Node getNodeInUnderlyingGraph(long id) {
        return null;
    }
}
