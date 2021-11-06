package solution.evaluation;

import model.SolvedPath;

import static solution.evaluation.SolutionEvaluator.evaluate;

public class SolutionEvaluation {
    private final SolvedPath solution;
    private final double evaluation;

    public SolutionEvaluation(SolvedPath solution) {
        this.solution = solution;
        this.evaluation = evaluate(solution);
    }

    public SolvedPath getSolution() {
        return solution;
    }

    public double getEvaluation() {
        return evaluation;
    }
}