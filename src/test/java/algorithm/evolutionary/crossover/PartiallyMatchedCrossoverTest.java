package algorithm.evolutionary.crossover;

import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.City;
import model.city.connection.CitiesConnectionConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static model.city.connection.CitiesConnectionConverter.convertCitiesListToCitiesConnections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PartiallyMatchedCrossoverTest {

    private CrossoverAlgorithm crossoverAlgorithm;
    private Repairer repairer;

    @BeforeEach
    void initialize() {
        repairer = new BasicRepairer(null, null);
        crossoverAlgorithm = new PartiallyMatchedCrossover(repairer);
    }

    @Test
    void givesCorrectChildForSimpleParents() {
        // given
        List<City> firstParentCities = Arrays.asList(
                new City(1, 0, 0, 50),
                new City(2, 0, 0, 50),
                new City(3, 0, 0, 50),
                new City(4, 0, 0, 50),
                new City(5, 0, 0, 50),
                new City(6, 0, 0, 50),
                new City(7, 0, 0, 50),
                new City(8, 0, 0, 50),
                new City(9, 0, 0, 50)
        );

        List<City> secondParentCities = Arrays.asList(
                new City(9, 0, 0, 50),
                new City(3, 0, 0, 50),
                new City(7, 0, 0, 50),
                new City(8, 0, 0, 50),
                new City(2, 0, 0, 50),
                new City(6, 0, 0, 50),
                new City(5, 0, 0, 50),
                new City(1, 0, 0, 50),
                new City(4, 0, 0, 50)
        );

        SolvedPath firstParent = new SolvedPath(convertCitiesListToCitiesConnections(firstParentCities));
        SolvedPath secondParent = new SolvedPath(convertCitiesListToCitiesConnections(secondParentCities));

        // when
        SolvedPath child = crossoverAlgorithm.crossover(firstParent, secondParent);

        // then
        List<City> expectedChildCities = Arrays.asList(
                new City(9, 0, 0, 50),
                new City(3, 0, 0, 50),
                new City(2, 0, 0, 50),
                new City(4, 0, 0, 50),
                new City(5, 0, 0, 50),
                new City(6, 0, 0, 50),
                new City(7, 0, 0, 50),
                new City(1, 0, 0, 50),
                new City(8, 0, 0, 50)
        );
        SolvedPath expectedChildPath = new SolvedPath(convertCitiesListToCitiesConnections(expectedChildCities));

        Assertions.assertThat(child).isEqualTo(expectedChildPath);
    }
}
