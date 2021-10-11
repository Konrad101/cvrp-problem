package solution;

import model.CvrpData;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.City;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.city.CityType.DELIVERY_CITY;

public class SolvedPathAssertion extends AbstractAssert<SolvedPathAssertion, SolvedPath> {

    private static final int DELIVERY_CITY_OCCURRENCES_AMOUNT = 2;

    private SolvedPathAssertion(SolvedPath solvedPath) {
        super(solvedPath, SolvedPathAssertion.class);
    }

    public static SolvedPathAssertion assertThatSolvedPath(SolvedPath solvedPath) {
        return new SolvedPathAssertion(solvedPath);
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

    private Map<City, Integer> getDeliveryCitiesOccurrences(List<CitiesConnection> connections) {
        Map<City, Integer> deliveryCitiesOccurrences = new HashMap<>();
        connections.forEach(connection -> {
            determineDeliveryCityOccurrence(deliveryCitiesOccurrences, connection.getOriginPlace());
            determineDeliveryCityOccurrence(deliveryCitiesOccurrences, connection.getDestinationPlace());
        });

        return deliveryCitiesOccurrences;
    }

    private void determineDeliveryCityOccurrence(Map<City, Integer> deliveryCitiesOccurrences, City city) {
        if (city.getCityType() == DELIVERY_CITY) {
            int originCityOccurrences = deliveryCitiesOccurrences.getOrDefault(city, 0);
            deliveryCitiesOccurrences.put(city, originCityOccurrences + 1);
        }
    }
}
