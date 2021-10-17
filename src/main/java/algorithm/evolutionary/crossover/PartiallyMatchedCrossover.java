package algorithm.evolutionary.crossover;

import algorithm.evolutionary.index.RandomIndexesProvider;
import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnection;

import java.util.*;
import java.util.stream.Collectors;

import static algorithm.evolutionary.index.RandomIndexesProvider.randomIndexRangeOf;
import static java.util.Objects.nonNull;
import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static model.city.connection.CitiesConnectionConverter.convertConnectionsToOrderedCityList;

public class PartiallyMatchedCrossover implements CrossoverAlgorithm {

    private final Repairer repairer;

    public PartiallyMatchedCrossover(Repairer repairer) {
        this.repairer = repairer;
    }

    @Override
    public SolvedPath crossover(SolvedPath firstParent, SolvedPath secondParent) {

        List<CitiesConnection> firstParentConnections = firstParent.getConnections();
        List<CitiesConnection> secondParentConnections = secondParent.getConnections();

        List<City> firstParentDeliveryCities = convertConnectionsToOrderedCityList(firstParentConnections).stream()
                .filter(City::isDeliveryCity)
                .collect(Collectors.toUnmodifiableList());
        List<City> secondParentDeliveryCities = convertConnectionsToOrderedCityList(secondParentConnections).stream()
                .filter(City::isDeliveryCity)
                .collect(Collectors.toUnmodifiableList());

        List<City> childCities = getChildCities(firstParentDeliveryCities, secondParentDeliveryCities);
        List<CitiesConnection> childConnections = convertCitiesListToCitiesConnections(childCities);

        return repairer.repairPath(new SolvedPath(childConnections));
    }

    private List<City> getChildCities(List<City> firstParentDeliveryCities, List<City> secondParentDeliveryCities) {
        RandomIndexesProvider randomIndexesProvider = randomIndexRangeOf(firstParentDeliveryCities.size());

        int firstParentIndexFrom = randomIndexesProvider.indexFrom();
        int firstParentIndexTo = randomIndexesProvider.indexTo();

        City[] childCities = new City[firstParentDeliveryCities.size()];

        for (int i = firstParentIndexFrom; i <= firstParentIndexTo; i++) {
            childCities[i] = firstParentDeliveryCities.get(i);
        }

        if (firstParentIndexFrom == 0 && firstParentIndexTo == firstParentDeliveryCities.size() - 1)
            return Arrays.asList(childCities);

        List<City> firstParentSublist = firstParentDeliveryCities.subList(firstParentIndexFrom, firstParentIndexTo + 1);
        for (int i = firstParentIndexFrom; i <= firstParentIndexTo; i++) {
            City cityToMap = secondParentDeliveryCities.get(i);

            if (!firstParentSublist.contains(cityToMap)) {
                mapCityElement(
                        cityToMap,
                        firstParentDeliveryCities,
                        secondParentDeliveryCities,
                        childCities
                );
            }
        }

        List<City> missingCities = secondParentDeliveryCities.stream()
                .filter(city -> !Arrays.asList(childCities).contains(city))
                .collect(Collectors.toUnmodifiableList());

        Iterator<City> missingCitiesIterator = missingCities.iterator();
        for (int i = 0; i < childCities.length; i++) {
            if (Objects.isNull(childCities[i])) {
                childCities[i] = missingCitiesIterator.next();
            }
        }

        return Arrays.asList(childCities);
    }

    private void mapCityElement(
            City cityToMap,
            List<City> firstParentDeliveryCities,
            List<City> secondParentDeliveryCities,
            City[] childCities) {

        int secondParentCityIndex = secondParentDeliveryCities.indexOf(cityToMap);

        City mappedFirstParentCity = firstParentDeliveryCities.get(secondParentCityIndex);
        int indexForMappedCity = secondParentDeliveryCities.indexOf(mappedFirstParentCity);

        if (nonNull(childCities[indexForMappedCity])) {
            mappedFirstParentCity = firstParentDeliveryCities.get(indexForMappedCity);
            indexForMappedCity = secondParentDeliveryCities.indexOf(mappedFirstParentCity);
        }

        childCities[indexForMappedCity] = cityToMap;
    }

}
