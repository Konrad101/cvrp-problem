package algorithm.evolutionary;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.Repairer;
import algorithm.result.AlgorithmResult;
import algorithm.result.PopulationResult;
import model.CvrpData;
import model.SolvedPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static algorithm.result.AlgorithmResult.extractResultFromEvaluations;
import static solution.evaluator.SolutionEvaluator.evaluate;

public class EvolutionaryAlgorithm implements PathResolverAlgorithm {

    private static final Random random = new Random();
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS_AMOUNT = 200;

    private static final double CROSSOVER_PROBABILITY = 0.7;
    private static final double MUTATION_PROBABILITY = 0.1;

    private final List<PopulationResult> results;

    private final SelectionAlgorithm selectionAlgorithm;
    private final CrossoverAlgorithm crossoverAlgorithm;
    private final Mutator mutator;
    private final Repairer repairer;

    public EvolutionaryAlgorithm(SelectionAlgorithm selectionAlgorithm,
                                 CrossoverAlgorithm crossoverAlgorithm,
                                 Mutator mutator,
                                 Repairer repairer) {
        this.selectionAlgorithm = selectionAlgorithm;
        this.crossoverAlgorithm = crossoverAlgorithm;
        this.mutator = mutator;
        this.repairer = repairer;

        results = new ArrayList<>();
    }

    public List<PopulationResult> getResults() {
        return results;
    }

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        List<SolvedPath> population = generateRandomPopulation(cvrpData);

        double shortestPath = Double.MAX_VALUE;
        SolvedPath bestSolution = population.get(0);

        for (SolvedPath path : population) {
            double pathEvaluation = evaluate(path);
            if (pathEvaluation < shortestPath) {
                shortestPath = pathEvaluation;
                bestSolution = path;
            }
        }

        int populationNumber = 0;
        while (populationNumber < GENERATIONS_AMOUNT) {
            List<SolvedPath> newPopulation = new ArrayList<>(POPULATION_SIZE);
            List<Double> newPopulationEvaluations = new ArrayList<>(POPULATION_SIZE);

            while (newPopulation.size() != POPULATION_SIZE) {
                SolvedPath child = createNewChild(population);

                newPopulation.add(child);

                double childEvaluation = evaluate(child);
                newPopulationEvaluations.add(childEvaluation);

                if (shortestPath > childEvaluation) {
                    shortestPath = childEvaluation;
                    bestSolution = child;
                }
            }

            ++populationNumber;
            population = newPopulation;
            results.add(extractPopulationResultFromEvaluations(newPopulationEvaluations, populationNumber));
        }

        return bestSolution;
    }

    private SolvedPath createNewChild(List<SolvedPath> population) {
        SolvedPath firstParent = selectionAlgorithm.selectFromPopulation(population);
        SolvedPath secondParent = selectionAlgorithm.selectFromPopulation(population);

        SolvedPath child = random.nextDouble() <= CROSSOVER_PROBABILITY ?
                crossoverAlgorithm.crossover(firstParent, secondParent) :
                firstParent;

        if (random.nextDouble() <= MUTATION_PROBABILITY) {
            child = mutator.mutation(child);
        }

        return child;
    }

    private PopulationResult extractPopulationResultFromEvaluations(List<Double> evaluations, int populationNumber) {
        AlgorithmResult algorithmResult = extractResultFromEvaluations(evaluations);

        double best = algorithmResult.getBestElementValue();
        double worst = algorithmResult.getWorstElementValue();
        double average = algorithmResult.getAverageElementValue();

        return new PopulationResult(populationNumber, best, worst, average);
    }

    private List<SolvedPath> generateRandomPopulation(CvrpData data) {
        List<SolvedPath> population = new ArrayList<>(POPULATION_SIZE);
        PathResolverAlgorithm pathResolver = new RandomPathResolver(repairer);

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(pathResolver.findOptimalPath(data));
        }

        return population;
    }
}
