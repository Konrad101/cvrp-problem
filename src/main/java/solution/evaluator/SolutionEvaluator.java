package solution.evaluator;

import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.CityDistanceCalculator;

import java.util.List;

public class SolutionEvaluator {

    private SolutionEvaluator() {
    }

    public static double evaluate(SolvedPath solution) {
        List<CitiesConnection> connections = solution.getConnections();

        return connections.stream()
                .map(CityDistanceCalculator::calculateDistance)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
