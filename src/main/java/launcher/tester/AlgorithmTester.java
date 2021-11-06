package launcher.tester;

import algorithm.PathResolverAlgorithm;
import algorithm.annealing.SimulatedAnnealingAlgorithm;
import algorithm.evolutionary.EvolutionaryAlgorithm;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.crossover.OrderedCrossover;
import algorithm.evolutionary.mutation.InvertMutation;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.evolutionary.selection.TournamentSelection;
import algorithm.greedy.GreedyPathResolver;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import algorithm.result.AlgorithmResult;
import algorithm.result.PopulationResult;
import algorithm.result.PopulationResultWithCurrentElement;
import algorithm.tabu.TabuSearchAlgorithm;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import solution.SolutionProviderService;

import java.util.ArrayList;
import java.util.List;

import static algorithm.result.AlgorithmResult.extractResultFromEvaluations;
import static solution.evaluation.SolutionEvaluator.evaluate;

public class AlgorithmTester {

    private static final String EA_RESULTS_PATH = "results\\EA_results.csv";
    private static final String TS_RESULTS_PATH = "results\\TS_results.csv";
    private static final String SA_RESULTS_PATH = "results\\SA_results.csv";

    private static final int EA_ALGORITHM_ITERATIONS = 10;
    private static final int TS_ALGORITHM_ITERATIONS = 10;
    private static final int SA_ALGORITHM_ITERATIONS = 10;
    private static final int RANDOM_ALGORITHM_ITERATIONS = 100;

    private final FileRepository repository;
    private final ResultPrinter resultPrinter;
    private final ResultCreator resultCreator;


    public AlgorithmTester(FileRepository repository) {
        this.repository = repository;
        this.resultPrinter = new ResultPrinter();
        this.resultCreator = new ResultCreator();
    }

    public void runSimulatedAnnealingAlgorithm(CvrpData cvrpData) {
        System.out.println("\nSIMULATED ANNEALING");

        Repairer repairer = getRepairerForCvrpData(cvrpData);
        Mutator mutator = new InvertMutation(repairer);

        List<List<PopulationResultWithCurrentElement>> resultsFromAllIterations = new ArrayList<>();
        for (int i = 0; i < SA_ALGORITHM_ITERATIONS; i++) {
            SimulatedAnnealingAlgorithm resolver = new SimulatedAnnealingAlgorithm(mutator);

            runAlgorithm(resolver, cvrpData);
            resultsFromAllIterations.add(resolver.getResults());
        }

        List<PopulationResultWithCurrentElement> finalResult =
                resultCreator.createResultFromAllIterationsSearchResultsWithCurrentElement(resultsFromAllIterations, SA_ALGORITHM_ITERATIONS);
        repository.saveAlgorithmResultWithCurrentElement(SA_RESULTS_PATH, finalResult);
        resultPrinter.printResultWithCurrentElement(finalResult);
    }

    public void runTabuSearchAlgorithm(CvrpData cvrpData) {
        System.out.println("\nTABU SEARCH");

        Repairer repairer = getRepairerForCvrpData(cvrpData);
        Mutator mutator = new InvertMutation(repairer);

        List<List<PopulationResultWithCurrentElement>> resultsFromAllIterations = new ArrayList<>();
        for (int i = 0; i < TS_ALGORITHM_ITERATIONS; i++) {
            TabuSearchAlgorithm resolver = new TabuSearchAlgorithm(mutator);

            runAlgorithm(resolver, cvrpData);
            resultsFromAllIterations.add(resolver.getResults());
        }

        List<PopulationResultWithCurrentElement> finalResult =
                resultCreator.createResultFromAllIterationsSearchResultsWithCurrentElement(resultsFromAllIterations, TS_ALGORITHM_ITERATIONS);
        repository.saveAlgorithmResultWithCurrentElement(TS_RESULTS_PATH, finalResult);
        resultPrinter.printResultWithCurrentElement(finalResult);
    }

    public void runEvolutionaryAlgorithm(CvrpData cvrpData) {
        System.out.println("\nEVOLUTIONARY");

        Repairer repairer = getRepairerForCvrpData(cvrpData);
        SelectionAlgorithm selectionAlgorithm = new TournamentSelection();
        CrossoverAlgorithm crossoverAlgorithm = new OrderedCrossover(repairer);
        Mutator mutator = new InvertMutation(repairer);

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

        List<PopulationResult> finalResult =
                resultCreator.createResultFromAllIterationsPopulationResults(resultsFromAllIterations, EA_ALGORITHM_ITERATIONS);

        repository.saveEvolutionaryAlgorithmResult(EA_RESULTS_PATH, finalResult);
        resultPrinter.printResultForEvolutionary(finalResult);
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
        resultPrinter.printResult(algorithmResult);
    }

    public void runGreedyAlgorithm(CvrpData cvrpData) {
        System.out.println("\nGREEDY");

        GreedyPathResolver resolver = new GreedyPathResolver();
        SolvedPath optimalPath = resolver.findOptimalPath(cvrpData);
        double evaluation = evaluate(optimalPath);

        System.out.println("value: " + evaluation);
    }

    private SolvedPath runAlgorithm(PathResolverAlgorithm resolver, CvrpData cvrpData) {
        SolutionProviderService providerService = new SolutionProviderService(resolver, cvrpData);
        return providerService.getSolution();
    }


    private Repairer getRepairerForCvrpData(CvrpData cvrpData) {
        return new BasicRepairer(cvrpData.getDepotCity(), cvrpData.getTruck());
    }

}
