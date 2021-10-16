package algorithm.reparation;

import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.City;
import model.truck.Truck;

import java.util.List;
import java.util.stream.Collectors;

import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

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

        List<City> deliveryCities = extractDeliveryCities(connections);

        List<CitiesConnection> pathConnections = convertCitiesListToCitiesConnections(deliveryCities);
        addDepotConnections(pathConnections);

        return new SolvedPath(pathConnections);
    }

    private List<City> extractDeliveryCities(List<CitiesConnection> connections) {
        List<City> cities = convertConnectionsToOrderedCityList(connections);
        return cities.stream()
                .filter(City::isDeliveryCity)
                .collect(Collectors.toUnmodifiableList());
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
