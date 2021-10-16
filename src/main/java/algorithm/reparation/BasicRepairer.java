package algorithm.reparation;

import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.City;
import model.truck.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicRepairer implements Repairer {

    private final City depotCity;
    private final Truck truck;

    public BasicRepairer(City depotCity, Truck truck) {
        this.depotCity = depotCity;
        this.truck = truck;
    }

    @Override
    public SolvedPath repairPath(SolvedPath path) {
        List<CitiesConnection> connections = path.getConnections();

        List<CitiesConnection> repairedPath = getConnectionsWithoutDepotConnections(connections);
        addDepotConnections(repairedPath);

        return new SolvedPath(repairedPath);
    }

    private List<CitiesConnection> getConnectionsWithoutDepotConnections(List<CitiesConnection> connections) {
        List<CitiesConnection> connectionsWithoutDepots = connections.stream()
                .filter(connection -> connection.getOriginPlace().isDeliveryCity() &&
                        connection.getDestinationPlace().isDeliveryCity())
                .collect(Collectors.toList());

        return addMissingConnections(connectionsWithoutDepots);
    }

    // adds connection when previous destination city is not equal to next origin city
    private List<CitiesConnection> addMissingConnections(List<CitiesConnection> connections) {
        List<CitiesConnection> repairedConnections = new ArrayList<>();

        CitiesConnection nextConnection = null;
        for (int i = 0; i < connections.size() - 1; i++) {
            CitiesConnection currentConnection = connections.get(i);
            nextConnection = connections.get(i + 1);

            repairedConnections.add(currentConnection);
            City currentDestinationPlace = currentConnection.getDestinationPlace();
            City nextOriginPlace = nextConnection.getOriginPlace();

            if(!currentDestinationPlace.equals(nextOriginPlace)) {
                repairedConnections.add(
                        new CitiesConnection(currentDestinationPlace, nextOriginPlace));
            }
        }
        repairedConnections.add(nextConnection);

        return repairedConnections;
    }

    private void addDepotConnections(List<CitiesConnection> connections) {
        connections.add(0,
                new CitiesConnection(
                        depotCity,
                        connections.get(0).getOriginPlace()));
        connections.add(connections.size(),
                new CitiesConnection(
                        connections.get(connections.size() - 1).getDestinationPlace(),
                        depotCity));
        truck.load();

        for (int i = 0; i < connections.size(); i++) {
            City connectionOriginPlace = connections.get(i).getOriginPlace();
            int demand = connectionOriginPlace.getDemand();

            // add additional connection to depot city
            if (truck.getGoodsAmount() < demand) {
                CitiesConnection previousConnection = connections.get(i - 1);
                previousConnection.setDestinationPlace(depotCity);
                truck.load();

                connections.add(i, new CitiesConnection(depotCity, connectionOriginPlace));
                i++;
            }

            truck.unload(demand);
        }
    }

}
