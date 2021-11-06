package algorithm.annealing;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import model.CvrpData;
import model.SolvedPath;

import static solution.evaluator.SolutionEvaluator.evaluate;

public class SimulatedAnnealingAlgorithm implements PathResolverAlgorithm {

    private static final double TEMPERATURE = 1000;
    private static final double COOLING_FACTOR = 0.99999;

    private final Mutator mutator;

    public SimulatedAnnealingAlgorithm(Mutator mutator) {
        this.mutator = mutator;
    }

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {

        SolvedPath bestSolution = initializeRandomSolution(cvrpData);
        double bestSolutionEvaluation = evaluate(bestSolution);

        SolvedPath currentSolution = bestSolution;

        double currentTemperature = TEMPERATURE;
        while (currentTemperature > 1) {

            SolvedPath neighbour = mutator.mutation(currentSolution);
            double neighbourEvaluation = evaluate(neighbour);

            if (Math.random() < probability(evaluate(currentSolution), neighbourEvaluation, currentTemperature)) {
                currentSolution = neighbour;
            }

            if (neighbourEvaluation < bestSolutionEvaluation) {
                bestSolutionEvaluation = neighbourEvaluation;
                bestSolution = currentSolution;
            }

            currentTemperature *= COOLING_FACTOR;
        }

        return bestSolution;
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
