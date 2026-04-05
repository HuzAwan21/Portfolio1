package routing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RouteImpl implements Route {

    private List<RouteLeg> legs = new ArrayList<>();

    public RouteImpl (List<RouteLeg> legs){
        this.legs = legs;
    }

    @Override
    public double distance() {
        double totalDistance = 0.0;
        for (RouteLeg leg : legs) {
            totalDistance += leg.getDistance();
        }
        return totalDistance;
    }

    @Override
    public Node getEndNode() {
        if (!legs.isEmpty()) {
            return legs.get(legs.size() - 1).getEndNode();
        }
        return null;
    }

    @Override
    public Node getStartNode() {
        if (!legs.isEmpty()) {
            return legs.get(0).getStartNode();
        }
        return null;
    }

    @Override
    public TravelType getTravelType() {
        if (legs.isEmpty()) {
            return null;
        }

        TravelType travelType = null;
        for (RouteLeg leg : legs) {
            if (leg instanceof RouteLegImpl) { 
                RouteLegImpl legImpl = (RouteLegImpl) leg;
                if (travelType == null) {
                    travelType = legImpl.getTravelType();
                } else if (!travelType.equals(legImpl.getTravelType())) {
                    return null;
                }
            }
        }

        return travelType;
    }

    @Override
    public Iterator<RouteLeg> iterator() {
        return legs.iterator();
    }

    @Override
    public int size() {
        return legs.size();
    }


    public void addLeg(RouteLeg leg) {
        legs.add(leg);
    }

    @Override
    public String toJSON(long time, List<Coordinate> waypoints) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
