package model.city.connection;

import model.city.City;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CitiesConnectionConverter {

    private CitiesConnectionConverter() {

    }

    public static List<City> convertConnectionsToOrderedCityList(List<CitiesConnection> connections) {
        List<City> cities = connections.stream()
                .map(CitiesConnection::getOriginPlace)
                .collect(Collectors.toList());

        City lastCity = connections.get(connections.size() - 1).getDestinationPlace();
        cities.add(lastCity);
        return cities;
    }

    public static List<CitiesConnection> convertCitiesListToCitiesConnections(List<City> cities) {
        List<CitiesConnection> connections = new ArrayList<>();

        for (int i = 0; i < cities.size() - 1; i++) {
            City currentCity = cities.get(i);
            City nextCity = cities.get(i + 1);

            connections.add(
                    new CitiesConnection(currentCity, nextCity));
        }

        return connections;
    }
}
