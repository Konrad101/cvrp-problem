package algorithm.evolutionary.mutation;

import algorithm.evolutionary.index.RandomIndexesProvider;
import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.City;

import java.util.List;

import static algorithm.evolutionary.index.RandomIndexesProvider.randomIndexRangeOf;
import static java.util.Collections.swap;
import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

public class SwapMutation implements Mutator {

    private final Repairer repairer;

    public SwapMutation(Repairer repairer) {
        this.repairer = repairer;
    }

    @Override
    public SolvedPath mutation(SolvedPath solution) {

        List<CitiesConnection> connections = solution.getConnections();
        List<City> cities = convertConnectionsToOrderedCityList(connections);

        swapTwoRandomCities(cities);

        connections = convertCitiesListToCitiesConnections(cities);

        return repairer.repairPath(new SolvedPath(connections));
    }

    private void swapTwoRandomCities(List<City> cities) {
        RandomIndexesProvider randomIndexesProvider = randomIndexRangeOf(cities.size());

        swap(cities,  randomIndexesProvider.indexFrom(), randomIndexesProvider.indexTo());
    }
}
