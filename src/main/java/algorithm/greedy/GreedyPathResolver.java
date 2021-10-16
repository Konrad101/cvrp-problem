package algorithm.greedy;

import algorithm.PathResolverAlgorithm;
import model.CvrpData;
import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.City;
import model.city.DeliveryCitiesMap;
import model.truck.Truck;

import java.util.*;

import static model.city.CityDistanceCalculator.calculateDistance;

public class GreedyPathResolver implements PathResolverAlgorithm {

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        DeliveryCitiesMap deliveryCitiesMap = cvrpData.getDeliveryCitiesMap();
        List<CitiesConnection> connections =
                findCompleteCitiesConnections(deliveryCitiesMap, cvrpData.getDepotCity(), cvrpData.getTruck());

        return new SolvedPath(connections);
    }

    private List<CitiesConnection> findCompleteCitiesConnections(
            DeliveryCitiesMap deliveryCitiesMap,
            City depotCity,
            Truck truck) {
        List<CitiesConnection> connections = new ArrayList<>();
        Collection<City> cities = deliveryCitiesMap.getCities();

        Set<City> citiesWithoutConnection = new HashSet<>(cities);

        City originCity = depotCity;
        truck.load();
        citiesWithoutConnection.remove(originCity);

        while (!citiesWithoutConnection.isEmpty()) {
            City destinationCity = getClosestCity(originCity, citiesWithoutConnection);

            if (isImpossibleToUnloadTruckInCity(destinationCity, truck)) {
                connections.add(
                        new CitiesConnection(originCity, depotCity));
                truck.load();
                originCity = depotCity;

                // find close city without connection from depot city
                destinationCity = getClosestCity(originCity, citiesWithoutConnection);
            }

            connections.add(
                    new CitiesConnection(originCity, destinationCity));
            truck.unload(destinationCity.getDemand());

            originCity = destinationCity;
            citiesWithoutConnection.remove(originCity);
        }

        addLastConnectionToDepotIfNecessary(connections, depotCity);
        return connections;
    }

    private void addLastConnectionToDepotIfNecessary(List<CitiesConnection> connections, City depotCity) {
        CitiesConnection lastConnection = connections.get(connections.size() - 1);
        City lastDestinationPlace = lastConnection.getDestinationPlace();

        if (lastDestinationPlace.equals(depotCity)) return;

        connections.add(
                new CitiesConnection(lastDestinationPlace, depotCity));
    }

    private boolean isImpossibleToUnloadTruckInCity(City city, Truck truck) {
        return truck.getGoodsAmount() < city.getDemand();
    }

    private City getClosestCity(City originCity, Collection<City> cities) {
        return Collections.min(
                cities,
                Comparator.comparing(city -> calculateDistance(originCity, city)));
    }
}
