package pathFinder;

import model.CvrpData;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.DeliveryCitiesMap;
import model.city.City;

import java.util.*;
import java.util.stream.Collectors;

public class RandomPathResolver implements PathResolver {

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        DeliveryCitiesMap deliveryCitiesMap = cvrpData.getDeliveryCitiesMap();
        List<CitiesConnection> citiesConnection = findCitiesConnections(deliveryCitiesMap);

        // dodaj magazyny

        return null;
    }

    private List<CitiesConnection> findCitiesConnections(DeliveryCitiesMap deliveryCitiesMap) {
        List<CitiesConnection> connections = new ArrayList<>();
        Collection<City> cities = deliveryCitiesMap.getCities();

        for (int i = 0; i < cities.size() - 1; i++) {
            connections.add(new CitiesConnection(
                    cities.iterator().next(),
                    getRandomCityWithoutConnection(connections, deliveryCitiesMap))
            );
        }

        return connections;
    }

    private City getRandomCityWithoutConnection(List<CitiesConnection> citiesConnection, DeliveryCitiesMap deliveryCitiesMap) {
        Collection<City> cities = deliveryCitiesMap.getCities();

        Set<City> citiesWithConnection = cities.stream()
                .filter(city -> citiesConnection.stream()
                        .anyMatch(connection -> connection.containsCity(city)))
                .collect(Collectors.toSet());

        Optional<City> cityWithoutConnection = cities.stream()
                .filter(city -> !citiesWithConnection.contains(city))
                .findAny();

        if (cityWithoutConnection.isEmpty()) {
            throw new IllegalArgumentException("Provided list of connections between cities already had all cities connected");
        }

        return cityWithoutConnection.get();
    }

}
