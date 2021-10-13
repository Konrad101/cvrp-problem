package algorithm.evolutionary.selection;

import model.SolvedPath;

import java.util.List;
import java.util.Random;

import static solution.evaluator.SolutionEvaluator.evaluate;


public class TournamentSelection implements SelectionAlgorithm {

    private static final double TOURNAMENT_SIZE = 0.07;
    private static final Random random = new Random();


    @Override
    public SolvedPath selectFromPopulation(List<SolvedPath> population) {
        int endIndex = (int) (TOURNAMENT_SIZE * population.size());
        int startIndex = random.nextInt(population.size() - endIndex);

        List<SolvedPath> selectedSolutions = population.subList(startIndex, endIndex);

        SolvedPath bestSolution = selectedSolutions.get(0);
        double bestSolutionEvaluation = evaluate(bestSolution);

        for (SolvedPath solution : selectedSolutions) {
            double solutionEvaluation = evaluate(solution);

            if (bestSolutionEvaluation > solutionEvaluation) {
                bestSolutionEvaluation = solutionEvaluation;
                bestSolution = solution;
            }
        }

        return bestSolution;
    }
}
