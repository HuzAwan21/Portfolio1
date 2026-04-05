package routing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeFactory implements Node {

    private long nodeID;
    private double latitude;
    private double longitude;
    private List<Edge> edges;

    public NodeFactory(long nodeID, double latitude, double longitude) {
        this.nodeID = nodeID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
    }

    @Override
    public Coordinate getCoordinate() {
        return new Coordinate(latitude, longitude);
    }

    @Override
    public Edge getEdge(int idx) {
        if (idx < 0 || idx >= edges.size()) {
            throw new IndexOutOfBoundsException("Invalid edge index");
        }
        return edges.get(idx);
    }

    @Override
    public long getId() {
        return nodeID;
    }

    @Override
    public Iterator<Edge> iterator() {
        return edges.iterator();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public void addEdge(Edge e) {
        edges.add(e);
    }

    @Override
    public void removeEdge(int indx) {
        if (indx < 0 || indx >= edges.size()) {
            throw new IndexOutOfBoundsException("Invalid edge index");
        }
        edges.remove(indx);
    }
}
