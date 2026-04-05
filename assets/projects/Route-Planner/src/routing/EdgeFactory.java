package routing;

public class EdgeFactory implements Edge {

    private Node start;
    private Node destination;
    private boolean forwardPassableByCar;
    private boolean backwardPassableByCar;
    private boolean forwardPassableByBike;
    private boolean backwardPassableByBike;
    private boolean forwardWalkable;
    private boolean backwardWalkable;

    public EdgeFactory(Node start, Node destination,
        boolean forwardPassableByCar, boolean backwardPassableByCar,
        boolean forwardPassableByBike, boolean backwardPassableByBike,
        boolean forwardWalkable, boolean backwardWalkable) {
            
        this.start = start;
        this.destination = destination;
        this.forwardPassableByCar = forwardPassableByCar;
        this.backwardPassableByCar = backwardPassableByCar;
        this.forwardPassableByBike = forwardPassableByBike;
        this.backwardPassableByBike = backwardPassableByBike;
        this.forwardWalkable = forwardWalkable;
        this.backwardWalkable = backwardWalkable;
    }

    @Override
    public boolean allowsTravelType(TravelType tt, Direction dir) {
        if (tt == null || dir == null) {
            throw new IllegalArgumentException("TravelType or Direction cannot be null");
        }

        switch (tt) {
            case CAR:
                if (dir == Direction.FORWARD) {
                    return forwardPassableByCar;
                } else if (dir == Direction.BACKWARD) {
                    return backwardPassableByCar;
                }
                break;
            case BIKE:
                if (dir == Direction.FORWARD) {
                    return forwardPassableByBike;
                } else if (dir == Direction.BACKWARD) {
                    return backwardPassableByBike;
                }
                break;
            case FOOT:
                if (dir == Direction.FORWARD) {
                    return forwardWalkable;
                } else if (dir == Direction.BACKWARD) {
                    return backwardWalkable;
                }
                break;
            case ANY:
                if (dir == Direction.FORWARD) {
                    return forwardWalkable || forwardPassableByBike || forwardPassableByCar;
                } else if (dir == Direction.BACKWARD) {
                    return backwardWalkable || backwardPassableByBike || backwardPassableByCar;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public double getLength() {
        Coordinate startCoord = start.getCoordinate();
        Coordinate destCoord = destination.getCoordinate();
        return startCoord.getDistance(destCoord);
    }

    @Override
    public Node getStart() {
        return this.start;
    }

    @Override
    public Node getEnd() {
        return this.destination;
    }
}
