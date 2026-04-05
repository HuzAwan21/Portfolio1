package routing;

import java.util.*;

public class NodeFinderGrid implements NodeFinder {
    private final Graph graph;
    private final Map<String, List<Node>> grid;
    private final double cellSize;

    public NodeFinderGrid(Graph graph, double cellSize) {
        this.graph = graph;
        this.grid = new HashMap<>();
        this.cellSize = cellSize;
        initializeGrid();
    }

    private void initializeGrid() {
        for (Node node : graph) {
            Coordinate coord = node.getCoordinate();
            String cellKey = getCellKey(coord);
    
            List<Node> nodeList = grid.get(cellKey);
    
            if (nodeList == null) {
                nodeList = new ArrayList<>();
                grid.put(cellKey, nodeList);
            }
    
            nodeList.add(node);
        }
    }

    private String getCellKey(Coordinate coord) {
        int lat = (int)((coord.getLatitude() - Coordinate.MIN_LAT) / cellSize);
        int lon = (int)((coord.getLongitude() - Coordinate.MIN_LON) / cellSize);
        return (lat + "_" + lon);
    }

    private List<String> getNearbyCellKeys(Coordinate coord, double radius) {
        List<String> keys = new ArrayList<>();
        CoordinateBox boundingBox = coord.computeBoundingBox(radius);
        int minlat = (int) ((boundingBox.getLowerBound().getLatitude() - Coordinate.MIN_LAT) / cellSize);
        int maxlat = (int) ((boundingBox.getUpperBound().getLatitude() - Coordinate.MIN_LAT) / cellSize);
        int minlon = (int) ((boundingBox.getLowerBound().getLongitude() - Coordinate.MIN_LON) / cellSize);
        int maxlon = (int) ((boundingBox.getUpperBound().getLongitude() - Coordinate.MIN_LON) / cellSize);

        for (int lat = minlat; lat <= maxlat; lat++) {
            for (int lon = minlon; lon <= maxlon; lon++) {
                keys.add(lat + "_" + lon);
            }
        }
        return keys;
    }

    @Override
    public Node getNodeForCoordinates(Coordinate coord) {
        double closestDistance = Double.MAX_VALUE;
        Node closestNode = null;
        double searchRadius = cellSize;
        boolean found = false;

        while (!found) {
            List<String> nearbyCellKeys = getNearbyCellKeys(coord, searchRadius);

            for (String key : nearbyCellKeys) {
                List<Node> nodes = grid.get(key);
                if (nodes != null) {
                    for (Node node : nodes) {
                        double distance = coord.getDistance(node.getCoordinate());
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestNode = node;
                            found = true;
                        }
                    }
                }
            }
            searchRadius *= 2;
        }
        return closestNode;
    }
}
