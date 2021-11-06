package algorithm.result;

import model.SolvedPath;
import solution.evaluation.SolutionEvaluation;

import java.util.Collection;
import java.util.stream.Collectors;

public class PopulationResultWithCurrentElement extends PopulationResult {

    private final double currentElementValue;

    public PopulationResultWithCurrentElement(
            int populationNumber,
            double bestElementValue,
            double worstElementValue,
            double averageElementValue,
            double currentElementValue) {

        super(populationNumber, bestElementValue, worstElementValue, averageElementValue);
        this.currentElementValue = currentElementValue;
    }

    public double getCurrentElementValue() {
        return currentElementValue;
    }

    public static PopulationResultWithCurrentElement extractResultWithCurrentElement(
            int iterationNumber,
            Collection<SolvedPath> neighbourhood,
            double bestSolutionEvaluation,
            double currentSolutionEvaluation) {

        Collection<SolutionEvaluation> neighboursEvaluations = neighbourhood.stream()
                .map(SolutionEvaluation::new)
                .collect(Collectors.toUnmodifiableList());

        return extractResultWithCurrentElementWithSolutionEvaluationNeighbourhood(
                iterationNumber,
                neighboursEvaluations,
                bestSolutionEvaluation,
                currentSolutionEvaluation
        );
    }

    public static PopulationResultWithCurrentElement extractResultWithCurrentElementWithSolutionEvaluationNeighbourhood(
            int iterationNumber,
            Collection<SolutionEvaluation> neighbourhood,
            double bestSolutionEvaluation,
            double currentSolutionEvaluation) {

        double sumOfNeighboursEvaluations = 0;
        double worstNeighbourEvaluation = Double.MIN_VALUE;

        for (SolutionEvaluation neighbour : neighbourhood) {

            double neighbourEvaluation = neighbour.getEvaluation();
            sumOfNeighboursEvaluations += neighbourEvaluation;

            if (worstNeighbourEvaluation < neighbourEvaluation) {
                worstNeighbourEvaluation = neighbourEvaluation;
            }
        }

        double averageSolutionEvaluation = sumOfNeighboursEvaluations / neighbourhood.size();

        return new PopulationResultWithCurrentElement(
                iterationNumber,
                bestSolutionEvaluation,
                worstNeighbourEvaluation,
                averageSolutionEvaluation,
                currentSolutionEvaluation
        );
    }
}
