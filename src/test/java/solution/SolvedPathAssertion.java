package solution;

import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnection;
import model.truck.Truck;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolvedPathAssertion extends AbstractAssert<SolvedPathAssertion, SolvedPath> {

    private static final int DELIVERY_CITY_OCCURRENCES_AMOUNT = 2;

    private SolvedPathAssertion(SolvedPath solvedPath) {
        super(solvedPath, SolvedPathAssertion.class);
    }

    public static SolvedPathAssertion assertThatSolvedPath(SolvedPath solvedPath) {
        return new SolvedPathAssertion(solvedPath);
    }

    // method should throw InsufficientGoodsQuantityException if there will be not enough goods in truck while unloading
    public SolvedPathAssertion isPossibleToRideByTruck(Truck truck) {
        isNotNull();

        List<CitiesConnection> connections = actual.getConnections();

        connections.forEach(
                connection -> resolveLoadingForCity(truck, connection.getOriginPlace()));

        resolveLoadingForCity(truck, connections.get(connections.size() - 1)
                .getDestinationPlace());

        return this;
    }

    public SolvedPathAssertion doesNotSkipCities() {
        isNotNull();

        List<CitiesConnection> connections = actual.getConnections();

        List<Integer> citiesNumbers = connections.stream()
                .map(connection -> connection.getOriginPlace().getNumber())
                .collect(Collectors.toUnmodifiableList());

        OptionalInt maxCityNumber = citiesNumbers.stream()
                .mapToInt(Integer::intValue)
                .max();

        maxCityNumber.ifPresent(
                maxNumber ->
                        IntStream.range(1, maxNumber)
                                .forEachOrdered(number ->
                                        Assertions.assertThat(citiesNumbers).contains(number))
        );

        return this;
    }

    public SolvedPathAssertion doesNotRepeatDepotCityAfterDepotCity() {
        isNotNull();

        List<CitiesConnection> connections = actual.getConnections();

        connections.forEach(connection -> {
            City originCity = connection.getOriginPlace();

            if (originCity.isDepotCity()) {
                City destinationCity = connection.getDestinationPlace();
                Assertions.assertThat(destinationCity.isDepotCity()).isFalse();
            }
        });

        return this;
    }

    public SolvedPathAssertion doesNotRepeatDeliveryCity() {
        isNotNull();

        List<CitiesConnection> connections = actual.getConnections();
        Map<City, Integer> deliveryCitiesOccurrences = getDeliveryCitiesOccurrences(connections);

        deliveryCitiesOccurrences.forEach(
                (city, occurrencesAmount) ->
                        Assertions.assertThat(occurrencesAmount).isEqualTo(DELIVERY_CITY_OCCURRENCES_AMOUNT)
        );

        return this;
    }

    public SolvedPathAssertion startsAndEndsWithDepotCity() {
        isNotNull();

        List<CitiesConnection> connections = actual.getConnections();
        City firstOriginCity = connections.get(0).getOriginPlace();
        City lastDestinationCity = connections.get(connections.size() - 1).getDestinationPlace();

        Assertions.assertThat(firstOriginCity.isDepotCity()).isTrue();
        Assertions.assertThat(lastDestinationCity.isDepotCity()).isTrue();

        return this;
    }

    private void resolveLoadingForCity(Truck truck, City city) {
        if (city.isDeliveryCity()) {
            truck.unload(city.getDemand());
        } else if (city.isDepotCity()) {
            truck.load();
        }
    }

    private Map<City, Integer> getDeliveryCitiesOccurrences(List<CitiesConnection> connections) {
        Map<City, Integer> deliveryCitiesOccurrences = new HashMap<>();
        connections.forEach(connection -> {
            determineDeliveryCityOccurrence(deliveryCitiesOccurrences, connection.getOriginPlace());
            determineDeliveryCityOccurrence(deliveryCitiesOccurrences, connection.getDestinationPlace());
        });

        return deliveryCitiesOccurrences;
    }

    private void determineDeliveryCityOccurrence(Map<City, Integer> deliveryCitiesOccurrences, City city) {
        if (city.isDeliveryCity()) {
            int originCityOccurrences = deliveryCitiesOccurrences.getOrDefault(city, 0);
            deliveryCitiesOccurrences.put(city, originCityOccurrences + 1);
        }
    }
}
