package algorithm.evolutionary.selection;

import model.SolvedPath;

import java.util.List;
import java.util.Random;

import static solution.evaluation.SolutionEvaluator.evaluate;


public class TournamentSelection implements SelectionAlgorithm {

    private static final double TOURNAMENT_SIZE = 5;
    private static final Random random = new Random();


    @Override
    public SolvedPath selectFromPopulation(List<SolvedPath> population) {
        int solutionsAmount = (int) (TOURNAMENT_SIZE < 1 ?
                TOURNAMENT_SIZE * population.size() :
                TOURNAMENT_SIZE
        );
        int startIndex = random.nextInt(population.size() - solutionsAmount);

        List<SolvedPath> selectedSolutions = population.subList(startIndex, startIndex + solutionsAmount);

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
