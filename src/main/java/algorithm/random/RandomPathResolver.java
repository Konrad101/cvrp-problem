package algorithm.random;

import algorithm.PathResolverAlgorithm;
import model.CvrpData;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.City;
import model.city.DeliveryCitiesMap;
import model.truck.Truck;

import java.util.*;

public class RandomPathResolver implements PathResolverAlgorithm {

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

        Set<City> citiesWithoutConnection = new HashSet<>(cities);

        City originCity = cities.iterator().next();
        citiesWithoutConnection.remove(originCity);

        while (!citiesWithoutConnection.isEmpty()) {
            City destinationCity = selectRandomDestinationCity(citiesWithoutConnection);
            connections.add(
                    new CitiesConnection(originCity, destinationCity));

            originCity = destinationCity;
            citiesWithoutConnection.remove(originCity);
        }

        return connections;
    }

    private City selectRandomDestinationCity(Collection<City> citiesWithoutConnection) {

        List<City> cities = citiesWithoutConnection instanceof ArrayList ?
                (ArrayList<City>) citiesWithoutConnection :
                new ArrayList<>(citiesWithoutConnection);

        return cities.get(random.nextInt(cities.size()));
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
