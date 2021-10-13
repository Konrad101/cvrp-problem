package algorithm.random;

import model.CvrpData;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.City;
import model.city.DeliveryCitiesMap;
import model.truck.Truck;
import algorithm.PathResolverAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

public class RandomPathResolverAlgorithm implements PathResolverAlgorithm {

    private static final Random random = new Random();

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        DeliveryCitiesMap deliveryCitiesMap = cvrpData.getDeliveryCitiesMap();
        List<CitiesConnection> citiesConnection = findCitiesConnections(deliveryCitiesMap);

        Truck truck = cvrpData.getTruck();
        City depotCity = cvrpData.getDepotCity();
        addDepotConnections(citiesConnection, depotCity, truck);

        return new SolvedPath(citiesConnection);
    }

    private List<CitiesConnection> findCitiesConnections(DeliveryCitiesMap deliveryCitiesMap) {
        List<CitiesConnection> connections = new ArrayList<>();
        Collection<City> cities = deliveryCitiesMap.getCities();

        List<City> citiesWithoutConnection = new ArrayList<>(cities);
        City destinationCity = cities.iterator().next();

        while (!citiesWithoutConnection.isEmpty()) {
            City originCity = destinationCity;
            citiesWithoutConnection.remove(originCity);
            destinationCity = selectRandomDestinationCity(citiesWithoutConnection);

            connections.add(
                    new CitiesConnection(originCity, destinationCity));

            citiesWithoutConnection = getCitiesWithoutConnection(connections, cities);
        }

        return connections;
    }

    private List<City> getCitiesWithoutConnection(List<CitiesConnection> connections, Collection<City> cities) {
        Set<City> citiesWithConnection = cities.stream()
                .filter(city -> connections.stream()
                        .anyMatch(connection -> connection.containsCity(city)))
                .collect(Collectors.toSet());

        return cities.stream()
                .filter(city -> !citiesWithConnection.contains(city))
                .collect(Collectors.toList());
    }

    private City selectRandomDestinationCity(List<City> citiesWithoutConnection) {
        return citiesWithoutConnection.get(
                random.nextInt(citiesWithoutConnection.size())
        );
    }

    private void addDepotConnections(List<CitiesConnection> connections, City depotCity, Truck truck) {
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
