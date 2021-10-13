package algorithm.evolutionary;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.random.RandomPathResolverAlgorithm;
import model.CvrpData;
import model.SolvedPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static solution.evaluator.SolutionEvaluator.evaluate;

public class EvolutionaryAlgorithm implements PathResolverAlgorithm {

    private static final Random random = new Random();
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS_AMOUNT = 100;

    private static final double CROSSOVER_PROBABILITY = 0.6;
    private static final double MUTATION_PROBABILITY = 0.05;

    private final List<PopulationResult> results;

    private final SelectionAlgorithm selectionAlgorithm;
    private final CrossoverAlgorithm crossoverAlgorithm;
    private final Mutator mutator;

    public EvolutionaryAlgorithm(SelectionAlgorithm selectionAlgorithm,
                                 CrossoverAlgorithm crossoverAlgorithm,
                                 Mutator mutator) {
        this.selectionAlgorithm = selectionAlgorithm;
        this.crossoverAlgorithm = crossoverAlgorithm;
        this.mutator = mutator;

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
        while (populationNumber <= GENERATIONS_AMOUNT) {
            List<SolvedPath> newPopulation = new ArrayList<>(POPULATION_SIZE);
            List<Double> newPopulationEvaluations = new ArrayList<>(POPULATION_SIZE);

            while (newPopulation.size() != POPULATION_SIZE) {
                SolvedPath child = createNewChild(population);
                mutator.mutation(child, MUTATION_PROBABILITY);

                newPopulation.add(child);

                double childEvaluation = evaluate(child);
                newPopulationEvaluations.add(childEvaluation);

                if(shortestPath > childEvaluation) {
                    shortestPath = childEvaluation;
                    bestSolution = child;
                }
            }

            ++populationNumber;
            population = newPopulation;
            results.add(extractResultFromEvaluations(newPopulationEvaluations));
        }

        return bestSolution;
    }

    private SolvedPath createNewChild(List<SolvedPath> population) {
        SolvedPath firstParent = selectionAlgorithm.selectFromPopulation(population);
        SolvedPath secondParent = selectionAlgorithm.selectFromPopulation(population);

        return random.nextDouble() <= CROSSOVER_PROBABILITY ?
                crossoverAlgorithm.crossover(firstParent, secondParent) :
                firstParent;
    }

    private PopulationResult extractResultFromEvaluations(List<Double> evaluations) {
        double best = evaluations.get(0);
        double worst= evaluations.get(0);
        double sumOfEvaluations = 0;

        for(Double evaluation : evaluations) {
            sumOfEvaluations += evaluation;

            if(best > evaluation) best = evaluation;
            if(worst < evaluation) worst = evaluation;
        }

        double average = sumOfEvaluations / evaluations.size();

        return new PopulationResult(best, worst, average);
    }

    private List<SolvedPath> generateRandomPopulation(CvrpData data) {
        List<SolvedPath> population = new ArrayList<>(POPULATION_SIZE);
        PathResolverAlgorithm pathResolver = new RandomPathResolverAlgorithm();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(pathResolver.findOptimalPath(data));
        }

        return population;
    }
}
