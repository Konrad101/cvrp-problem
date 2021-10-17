package launcher;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.EvolutionaryAlgorithm;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.crossover.OrderedCrossover;
import algorithm.evolutionary.crossover.PartiallyMatchedCrossover;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.mutation.SwapMutation;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.evolutionary.selection.TournamentSelection;
import algorithm.greedy.GreedyPathResolver;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import solution.SolutionPresenter;
import solution.SolutionProviderService;

public class Main {

    private static final String CVRP_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";

    public static void main(String[] args) {
        FileRepository repository = new CvrpFileDataRepository();
        CvrpData cvrpData = repository.getCvrpData(CVRP_PROBLEM_FILE_PATH);
        System.out.println(cvrpData.getComment());

        Repairer repairer = new BasicRepairer(cvrpData.getDepotCity(), cvrpData.getTruck());
        SelectionAlgorithm selectionAlgorithm = new TournamentSelection();
        CrossoverAlgorithm crossoverAlgorithm = new PartiallyMatchedCrossover(repairer);
        Mutator mutator = new SwapMutation(repairer);

        PathResolverAlgorithm resolver = evolutionaryAlgorithm(
                selectionAlgorithm,
                crossoverAlgorithm,
                mutator,
                repairer);

        SolutionProviderService providerService = new SolutionProviderService(resolver, cvrpData);

        SolvedPath solution = providerService.getSolution();
        SolutionPresenter solutionPresenter = new SolutionPresenter(solution, cvrpData);
        solutionPresenter.printSolution();
        solutionPresenter.printEvaluation();
    }

    public static RandomPathResolver randomPathResolver(Repairer repairer) {
        return new RandomPathResolver(repairer);
    }

    public static GreedyPathResolver greedyPathResolver() {
        return new GreedyPathResolver();
    }

    public static EvolutionaryAlgorithm evolutionaryAlgorithm(SelectionAlgorithm selectionAlgorithm,
                                                              CrossoverAlgorithm crossoverAlgorithm,
                                                              Mutator mutator,
                                                              Repairer repairer) {
        return new EvolutionaryAlgorithm(
                selectionAlgorithm,
                crossoverAlgorithm,
                mutator,
                repairer);
    }
}
