package launcher;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.EvolutionaryAlgorithm;
import algorithm.evolutionary.PopulationResult;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.crossover.OrderedCrossover;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.mutation.SwapMutation;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.evolutionary.selection.TournamentSelection;
import algorithm.greedy.GreedyPathResolver;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import algorithm.result.AlgorithmResult;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import solution.SolutionProviderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static algorithm.result.AlgorithmResult.calculateStandardDeviation;
import static algorithm.result.AlgorithmResult.extractResultFromEvaluations;
import static solution.evaluator.SolutionEvaluator.evaluate;

public class AlgorithmTester {

    private static final String EA_RESULTS_PATH = "results\\EA_results.csv";

    private static final int EA_ALGORITHM_ITERATIONS = 10;
    private static final int RANDOM_ALGORITHM_ITERATIONS = 10000;

    private final FileRepository repository;

    public AlgorithmTester(FileRepository repository) {
        this.repository = repository;
    }

    public void runEvolutionaryAlgorithm(CvrpData cvrpData) {
        System.out.println("\nEVOLUTIONARY");

        Repairer repairer = getRepairerForCvrpData(cvrpData);
        SelectionAlgorithm selectionAlgorithm = new TournamentSelection();
        CrossoverAlgorithm crossoverAlgorithm = new OrderedCrossover(repairer);
        Mutator mutator = new SwapMutation(repairer);

        List<List<PopulationResult>> resultsFromAllIterations = new ArrayList<>();
        for (int i = 0; i < EA_ALGORITHM_ITERATIONS; i++) {
            EvolutionaryAlgorithm resolver = new EvolutionaryAlgorithm(
                    selectionAlgorithm,
                    crossoverAlgorithm,
                    mutator,
                    repairer);

            runAlgorithm(resolver, cvrpData);
            resultsFromAllIterations.add(resolver.getResults());
        }

        List<PopulationResult> finalResult = createResultFromAllIterationPopulationResults(resultsFromAllIterations);
        repository.saveEvolutionaryAlgorithmResult(EA_RESULTS_PATH, finalResult);
        printResultForEvolutionary(finalResult);
    }

    public void runRandomAlgorithm(CvrpData cvrpData) {
        System.out.println("\nRANDOM");
        Repairer repairer = getRepairerForCvrpData(cvrpData);
        RandomPathResolver resolver = new RandomPathResolver(repairer);

        List<Double> algorithmEvaluations = new ArrayList<>();
        for (int i = 0; i < RANDOM_ALGORITHM_ITERATIONS; i++) {
            SolvedPath solution = runAlgorithm(resolver, cvrpData);
            algorithmEvaluations.add(evaluate(solution));
        }

        AlgorithmResult algorithmResult = extractResultFromEvaluations(algorithmEvaluations);
        printResult(algorithmResult);
    }

    public void runGreedyAlgorithm(CvrpData cvrpData) {
        System.out.println("\nGREEDY");

        GreedyPathResolver resolver = new GreedyPathResolver();
        SolvedPath optimalPath = resolver.findOptimalPath(cvrpData);
        double evaluation = evaluate(optimalPath);

        System.out.println("value: " + evaluation);
    }

    private void printResultForEvolutionary(List<PopulationResult> results) {
        PopulationResult firstResult = results.get(0);
        double best = firstResult.getBestElementValue();
        double worst = firstResult.getWorstElementValue();
        double sumOfAverages = 0;

        List<Double> averageNumbers = new ArrayList<>();
        for (PopulationResult result : results) {
            double avgValue = result.getAverageElementValue();
            averageNumbers.add(avgValue);
            sumOfAverages += avgValue;

            double worstElementValue = result.getWorstElementValue();
            if (worstElementValue > worst) worst = worstElementValue;

            double bestElementValue = result.getBestElementValue();
            if (bestElementValue < best) best = bestElementValue;
        }

        double average = sumOfAverages / results.size();
        double standardDeviation = calculateStandardDeviation(averageNumbers);

        AlgorithmResult algorithmResult = new AlgorithmResult(best, worst, average);
        algorithmResult.setStandardDeviation(standardDeviation);
        printResult(algorithmResult);
    }

    private void printResult(AlgorithmResult algorithmResult) {
        System.out.println("best: " + algorithmResult.getBestElementValue());
        System.out.println("worst: " + algorithmResult.getWorstElementValue());
        System.out.println("average: " + algorithmResult.getAverageElementValue());
        System.out.println("std: " + algorithmResult.getStandardDeviation());
    }

    private SolvedPath runAlgorithm(PathResolverAlgorithm resolver, CvrpData cvrpData) {
        SolutionProviderService providerService = new SolutionProviderService(resolver, cvrpData);
        return providerService.getSolution();
    }

    private List<PopulationResult> createResultFromAllIterationPopulationResults(List<List<PopulationResult>> results) {
        // key is population number
        Map<Integer, Double> sumOfBestsForEachPopulation = new HashMap<>();
        Map<Integer, Double> sumOfAveragesForEachPopulation = new HashMap<>();
        Map<Integer, Double> sumOfWorstsForEachPopulation = new HashMap<>();

        results.forEach(
                oneIterationResults -> oneIterationResults
                        .forEach(result -> {
                            sumOfBestsForEachPopulation.put(result.getPopulationNumber(), 0.);
                            sumOfWorstsForEachPopulation.put(result.getPopulationNumber(), 0.);
                            sumOfAveragesForEachPopulation.put(result.getPopulationNumber(), 0.);
                        }));

        results.forEach(
                oneIterationResults -> oneIterationResults.forEach(result -> {
                    int populationNumber = result.getPopulationNumber();

                    Double sumOfBests = sumOfBestsForEachPopulation.get(populationNumber);
                    Double sumOfWorsts = sumOfWorstsForEachPopulation.get(populationNumber);
                    Double sumOfAverages = sumOfAveragesForEachPopulation.get(populationNumber);

                    sumOfBestsForEachPopulation.put(populationNumber, sumOfBests + result.getBestElementValue());
                    sumOfWorstsForEachPopulation.put(populationNumber, sumOfWorsts + result.getWorstElementValue());
                    sumOfAveragesForEachPopulation.put(populationNumber, sumOfAverages + result.getAverageElementValue());
                }));

        int populationsAmount = sumOfBestsForEachPopulation.size();

        List<PopulationResult> populationResults = new ArrayList<>();
        for (int populationNumber = 1; populationNumber <= populationsAmount; populationNumber++) {
            Double sumOfBestsForCurrentPopulation = sumOfBestsForEachPopulation.get(populationNumber);
            Double sumOfWorstsForCurrentPopulation = sumOfWorstsForEachPopulation.get(populationNumber);
            Double sumOfAveragesForCurrentPopulation = sumOfAveragesForEachPopulation.get(populationNumber);

            double averageBest = sumOfBestsForCurrentPopulation / EA_ALGORITHM_ITERATIONS;
            double averageWorst = sumOfWorstsForCurrentPopulation / EA_ALGORITHM_ITERATIONS;
            double averageFromAverages = sumOfAveragesForCurrentPopulation / EA_ALGORITHM_ITERATIONS;

            populationResults.add(new PopulationResult(
                    populationNumber,
                    averageBest,
                    averageWorst,
                    averageFromAverages
            ));
        }

        return populationResults;
    }

    private Repairer getRepairerForCvrpData(CvrpData cvrpData) {
        return new BasicRepairer(cvrpData.getDepotCity(), cvrpData.getTruck());
    }

}
