package routing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RouteLegImpl extends RouteLegBase {

    private LinkedList<Node> nodes;
    private TravelType travelType; 

    public RouteLegImpl(List<Node> nodes, TravelType travelType) {
        this.nodes = new LinkedList<>(nodes);
        this.travelType = travelType;
    }

    @Override
    public double getDistance() {
        double distance = 0.0;
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node start = nodes.get(i);
            Node end = nodes.get(i + 1);
            for (Edge edge : start){
                if (edge.getStart().equals(start) && edge.getEnd().equals(end)){
                distance += edge.getLength();
                }
            }
        }
        return distance;
    }

    @Override
    public Node getEndNode() {
        return nodes.getLast();
    }

    @Override
    public Node getStartNode() {
        return nodes.getFirst();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    public TravelType getTravelType() {
        return travelType;
    }

    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
