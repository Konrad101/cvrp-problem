package algorithm.evolutionary.selection;

import model.SolvedPath;
import solution.evaluation.SolutionEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static solution.evaluation.SolutionEvaluator.evaluate;

public class RouletteSelection implements SelectionAlgorithm {

    private static final Random random = new Random();

    @Override
    public SolvedPath selectFromPopulation(List<SolvedPath> population) {
        double sumOfEvaluations = population.stream()
                .mapToDouble(SolutionEvaluator::evaluate)
                .sum();

        List<SolutionProbability> solutionsProbabilities = getProbabilitiesForSolutions(population, sumOfEvaluations);

        double observation = random.nextDouble();
        for (SolutionProbability solutionProbability : solutionsProbabilities) {
            observation -= solutionProbability.probability;
            if (observation <= 0) {
                return solutionProbability.solution;
            }
        }

        return population.get(0);
    }

    private List<SolutionProbability> getProbabilitiesForSolutions(List<SolvedPath> population, double sumOfEvaluations) {
        List<SolutionProbability> solutionsProbabilities = new ArrayList<>();
        for (SolvedPath solution : population) {
            double evaluation = evaluate(solution);
            double probability = 1 - (evaluation / sumOfEvaluations); // probability for minimization problem
            solutionsProbabilities.add(new SolutionProbability(solution, probability));
        }

        return solutionsProbabilities;
    }

    private static class SolutionProbability {
        private final SolvedPath solution;
        private final double probability;

        public SolutionProbability(SolvedPath solution, double probability) {
            this.solution = solution;
            this.probability = probability;
        }
    }
}
