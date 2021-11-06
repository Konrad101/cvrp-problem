package algorithm.tabu;

import algorithm.PathResolverAlgorithm;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.random.RandomPathResolver;
import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import algorithm.result.PopulationResultWithCurrentElement;
import model.CvrpData;
import model.SolvedPath;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import solution.evaluation.SolutionEvaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static algorithm.result.PopulationResultWithCurrentElement.extractResultWithCurrentElementWithSolutionEvaluationNeighbourhood;

public class TabuSearchAlgorithm implements PathResolverAlgorithm {

    private static final int MAX_ALGORITHM_ITERATIONS = 1000;
    private static final int NEIGHBOURHOOD_SIZE = 10;
    private static final int TABU_SIZE = 100;

    private final List<PopulationResultWithCurrentElement> results;

    private final Mutator mutator;

    public TabuSearchAlgorithm(Mutator mutator) {
        this.mutator = mutator;
        this.results = new ArrayList<>();
    }

    public List<PopulationResultWithCurrentElement> getResults() {
        return results;
    }

    @Override
    public SolvedPath findOptimalPath(CvrpData cvrpData) {
        Collection<SolvedPath> tabuCollection = new CircularFifoBuffer(TABU_SIZE);

        SolutionEvaluation currentSolutionEvaluation = initializeRandomSolution(cvrpData);
        SolutionEvaluation bestSolutionEvaluation = currentSolutionEvaluation;

        int iterationNumber = 0;
        while (iterationNumber < MAX_ALGORITHM_ITERATIONS) {
            Collection<SolutionEvaluation> neighbourhood = getNeighboursForSolution(currentSolutionEvaluation.getSolution());

            SolutionEvaluation bestNeighbour = extractBestNeighbour(neighbourhood, tabuCollection);

            if (bestNeighbour.getEvaluation() < bestSolutionEvaluation.getEvaluation()) {
                bestSolutionEvaluation = bestNeighbour;
            }

            tabuCollection.add(currentSolutionEvaluation.getSolution());
            currentSolutionEvaluation = bestNeighbour;

            iterationNumber++;
            results.add(extractResultWithCurrentElementWithSolutionEvaluationNeighbourhood(
                    iterationNumber,
                    neighbourhood,
                    bestSolutionEvaluation.getEvaluation(),
                    currentSolutionEvaluation.getEvaluation()));
        }

        return bestSolutionEvaluation.getSolution();
    }

    private List<SolutionEvaluation> getNeighboursForSolution(SolvedPath solution) {
        List<SolutionEvaluation> neighbours = new ArrayList<>();

        for (int i = 0; i < NEIGHBOURHOOD_SIZE; i++) {
            SolvedPath neighbour = mutator.mutation(solution);
            neighbours.add(
                    new SolutionEvaluation(neighbour)
            );
        }

        return neighbours;
    }

    private SolutionEvaluation extractBestNeighbour(Collection<SolutionEvaluation> neighbourhood, Collection<SolvedPath> tabuSolutions) {
        List<SolutionEvaluation> neighboursNotPresentInTabu = neighbourhood.stream()
                .filter(neighbour -> !tabuSolutions.contains(neighbour.getSolution()))
                .collect(Collectors.toUnmodifiableList());

        SolutionEvaluation bestNeighbour = neighboursNotPresentInTabu.get(0);
        for (SolutionEvaluation neighbour : neighboursNotPresentInTabu) {
            if (neighbour.getEvaluation() < bestNeighbour.getEvaluation()) {
                bestNeighbour = neighbour;
            }
        }

        return bestNeighbour;
    }

    private SolutionEvaluation initializeRandomSolution(CvrpData cvrpData) {
        Repairer repairer = getRepairer(cvrpData);
        PathResolverAlgorithm resolver = new RandomPathResolver(repairer);

        SolvedPath randomPath = resolver.findOptimalPath(cvrpData);
        return new SolutionEvaluation(randomPath);
    }

    private Repairer getRepairer(CvrpData cvrpData) {
        return new BasicRepairer(cvrpData.getDepotCity(), cvrpData.getTruck());
    }

}
