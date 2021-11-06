package solution.evaluation;

import model.SolvedPath;
import model.city.connection.CitiesConnection;
import model.city.CityDistanceCalculator;

import java.util.List;

public class SolutionEvaluator {

    private SolutionEvaluator() {
    }

    public static double evaluate(SolvedPath solution) {
        List<CitiesConnection> connections = solution.getConnections();

        return connections.stream()
                .mapToDouble(CityDistanceCalculator::calculateDistance)
                .sum();
    }
}
