package algorithm.annealing;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import algorithm.result.PopulationResultWithCurrentElement;
import model.CvrpData;
import model.SolvedPath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static algorithm.result.PopulationResultWithCurrentElement.extractResultWithCurrentElement;
import static solution.evaluation.SolutionEvaluator.evaluate;

public class SimulatedAnnealingAlgorithm implements PathResolverAlgorithm {

    private static final int MAX_ALGORITHM_ITERATIONS = 10000;
    private static final int NEIGHBOURHOOD_SIZE = 10;

    private static final double TEMPERATURE = 5000;
    private static final double END_TEMPERATURE = 1;
    private static final double COOLING_FACTOR = 0.99999;

    private final List<PopulationResultWithCurrentElement> results;

    private final Mutator mutator;

    public SimulatedAnnealingAlgorithm(Mutator mutator) {
        this.mutator = mutator;
        this.results = new ArrayList<>();
    }

    public List<PopulationResultWithCurrentElement> getResults() {
        return results;
    }

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {

        SolvedPath bestSolution = initializeRandomSolution(cvrpData);
        double bestSolutionEvaluation = evaluate(bestSolution);

        SolvedPath currentSolution = bestSolution;

        int iterationNumber = 0;
        double currentTemperature = TEMPERATURE;

        while (!stopConditionOccurs(iterationNumber, currentTemperature)) {
            List<SolvedPath> neighbourhood = getNeighboursForSolution(currentSolution);

            SolvedPath neighbour = extractBestNeighbour(neighbourhood);
            double neighbourEvaluation = evaluate(neighbour);

            if (evaluate(currentSolution) < neighbourEvaluation
                    || Math.random() < probability(evaluate(currentSolution), neighbourEvaluation, currentTemperature)) {
                currentSolution = neighbour;
            }

            if (neighbourEvaluation < bestSolutionEvaluation) {
                bestSolutionEvaluation = neighbourEvaluation;
                bestSolution = currentSolution;
            }

            currentTemperature *= COOLING_FACTOR;
            iterationNumber++;
            results.add(extractResultWithCurrentElement(
                    iterationNumber,
                    neighbourhood,
                    bestSolutionEvaluation,
                    evaluate(currentSolution)));
        }

        return bestSolution;
    }

    private SolvedPath extractBestNeighbour(List<SolvedPath> neighbourhood) {
        SolvedPath bestNeighbour = neighbourhood.get(0);
        double bestEvaluation = Double.MAX_VALUE;

        for (SolvedPath neighbour : neighbourhood) {
            double neighbourEvaluation = evaluate(neighbour);

            if (neighbourEvaluation < bestEvaluation) {
                bestEvaluation = neighbourEvaluation;
                bestNeighbour = neighbour;
            }
        }

        return bestNeighbour;
    }

    private List<SolvedPath> getNeighboursForSolution(SolvedPath currentSolution) {
        return IntStream.range(0, NEIGHBOURHOOD_SIZE)
                .mapToObj(i -> mutator.mutation(currentSolution))
                .collect(Collectors.toUnmodifiableList());
    }


    private boolean stopConditionOccurs(int currentAlgorithmIteration, double currentTemperature) {
        return currentAlgorithmIteration > MAX_ALGORITHM_ITERATIONS
                || currentTemperature <= END_TEMPERATURE;
    }

    private double probability(double firstEvaluation, double secondEvaluation, double temperature) {
        if (secondEvaluation < firstEvaluation) return 1;
        return Math.exp((firstEvaluation - secondEvaluation) / temperature);
    }

    private SolvedPath initializeRandomSolution(CvrpData cvrpData) {
        PathResolverAlgorithm randomPathResolver =
                new RandomPathResolver(new BasicRepairer(cvrpData.getDepotCity(), cvrpData.getTruck()));
        return randomPathResolver.findOptimalPath(cvrpData);
    }

}
