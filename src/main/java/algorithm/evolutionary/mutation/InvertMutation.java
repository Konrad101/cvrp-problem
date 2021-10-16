package algorithm.evolutionary.mutation;

import algorithm.evolutionary.index.RandomIndexesProvider;
import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnection;

import java.util.List;

import static algorithm.evolutionary.index.RandomIndexesProvider.randomIndexRangeOf;
import static java.util.Collections.reverse;
import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

public class InvertMutation implements Mutator {

    private final Repairer repairer;

    public InvertMutation(Repairer repairer) {
        this.repairer = repairer;
    }

    @Override
    public SolvedPath mutation(SolvedPath solution) {
        List<CitiesConnection> connections = solution.getConnections();
        List<City> cities = convertConnectionsToOrderedCityList(connections);

        invertCities(cities);

        connections = convertCitiesListToCitiesConnections(cities);

        return repairer.repairPath(new SolvedPath(connections));
    }

    private void invertCities(List<City> cities) {
        RandomIndexesProvider randomIndexesProvider = randomIndexRangeOf(cities.size());

        reverse(cities.subList(
                randomIndexesProvider.indexFrom(),
                randomIndexesProvider.indexTo()
        ));
    }
}
