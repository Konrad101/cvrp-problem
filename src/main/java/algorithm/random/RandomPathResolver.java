package algorithm.random;

import algorithm.PathResolverAlgorithm;
import algorithm.reparation.Repairer;
import model.CvrpData;
import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.City;
import model.city.DeliveryCitiesMap;

import java.util.*;

public class RandomPathResolver implements PathResolverAlgorithm {

    private static final Random random = new Random();

    private final Repairer repairer;

    public RandomPathResolver(Repairer repairer) {
        this.repairer = repairer;
    }

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        DeliveryCitiesMap deliveryCitiesMap = cvrpData.getDeliveryCitiesMap();
        List<CitiesConnection> citiesConnection = findCitiesConnections(deliveryCitiesMap);

        return repairer.repairPath(new SolvedPath(citiesConnection));
    }

    private List<CitiesConnection> findCitiesConnections(DeliveryCitiesMap deliveryCitiesMap) {
        List<CitiesConnection> connections = new ArrayList<>();
        Collection<City> cities = deliveryCitiesMap.getCities();

        Set<City> citiesWithoutConnection = new HashSet<>(cities);

        City originCity = selectRandomDestinationCity(citiesWithoutConnection);
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

}
