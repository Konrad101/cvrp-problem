package algorithm.evolutionary.mutation;

import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnection;

import java.util.List;
import java.util.Random;

import static java.util.Collections.reverse;
import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

public class InvertMutation implements Mutator {

    private static final Random random = new Random();

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
        int firstCityIndex = getRandomIndex(cities);
        int secondCityIndex;

        // to make sure that both indexes are different
        do {
            secondCityIndex = getRandomIndex(cities);
        } while (firstCityIndex == secondCityIndex);

        if (firstCityIndex > secondCityIndex) {
            int indexToSwap = firstCityIndex;
            firstCityIndex = secondCityIndex;
            secondCityIndex = indexToSwap;
        }

        reverse(cities.subList(firstCityIndex, secondCityIndex + 1));
    }

    private int getRandomIndex(List<City> cities) {
        return random.nextInt(cities.size());
    }

}
