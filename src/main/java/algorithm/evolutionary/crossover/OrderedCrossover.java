package algorithm.evolutionary.crossover;

import algorithm.evolutionary.index.RandomIndexesProvider;
import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static algorithm.evolutionary.index.RandomIndexesProvider.randomIndexRangeOf;
import static java.lang.Math.min;
import static java.util.stream.Stream.concat;
import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

public class OrderedCrossover implements CrossoverAlgorithm {

    private final Repairer repairer;

    public OrderedCrossover(Repairer repairer) {
        this.repairer = repairer;
    }


    @Override
    public SolvedPath crossover(SolvedPath firstParent, SolvedPath secondParent) {

        List<CitiesConnection> firstParentConnections = firstParent.getConnections();
        List<CitiesConnection> secondParentConnections = secondParent.getConnections();

        List<City> firstParentCities = convertConnectionsToOrderedCityList(firstParentConnections);
        List<City> secondParentCities = convertConnectionsToOrderedCityList(secondParentConnections);

        List<City> childCities = combineParentCities(firstParentCities, secondParentCities);
        List<CitiesConnection> childConnections = convertCitiesListToCitiesConnections(childCities);

        return repairer.repairPath(new SolvedPath(childConnections));
    }

    private List<City> combineParentCities(List<City> firstParentCities, List<City> secondParentCities) {

        RandomIndexesProvider randomIndexesProvider = randomIndexRangeOf(firstParentCities.size());

        int firstParentIndexFrom = randomIndexesProvider.indexFrom();
        int firstParentIndexTo = randomIndexesProvider.indexTo();

        List<City> firstParentCitiesSublist = firstParentCities.subList(firstParentIndexFrom, firstParentIndexTo);
        List<City> missingCities = secondParentCities.stream()
                .filter(city -> !firstParentCitiesSublist.contains(city) && city.isDeliveryCity())
                .collect(Collectors.toUnmodifiableList());

        if (missingCities.isEmpty()) {
            return firstParentCitiesSublist;
        }

        return concatMissingCities(
                new ArrayList<>(firstParentCitiesSublist),
                missingCities,
                firstParentIndexFrom,
                firstParentIndexTo,
                firstParentCities.size() - 1);
    }

    private List<City> concatMissingCities(
            List<City> childCities,
            List<City> missingCities,
            int firstParentIndexFrom,
            int firstParentIndexTo,
            int firstParentCitiesLastIndex) {

        if (firstParentIndexFrom > 0) {

            int missingFirstPartBound = min(missingCities.size(), firstParentIndexFrom);
            List<City> firstPartOfMissingCities = missingCities.subList(0, missingFirstPartBound);
            childCities = concat(
                    firstPartOfMissingCities.stream(),
                    childCities.stream()
            ).collect(Collectors.toUnmodifiableList());

            missingCities = missingCities.stream()
                    .filter(city -> !firstPartOfMissingCities.contains(city))
                    .collect(Collectors.toUnmodifiableList());
        }

        if (firstParentIndexTo < firstParentCitiesLastIndex) {
            childCities = concat(
                    childCities.stream(),
                    missingCities.stream()
            ).collect(Collectors.toUnmodifiableList());
        }

        return childCities;
    }

}
