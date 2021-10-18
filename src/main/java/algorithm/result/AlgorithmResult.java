package algorithm.result;

import java.util.List;

public class AlgorithmResult {
    private final double bestElementValue;
    private final double averageElementValue;
    private final double worstElementValue;

    private double standardDeviation;

    public AlgorithmResult(
            double bestElementValue,
            double worstElementValue,
            double averageElementValue) {
        this.bestElementValue = bestElementValue;
        this.worstElementValue = worstElementValue;
        this.averageElementValue = averageElementValue;
    }

    public double getBestElementValue() {
        return bestElementValue;
    }

    public double getAverageElementValue() {
        return averageElementValue;
    }

    public double getWorstElementValue() {
        return worstElementValue;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public static AlgorithmResult extractResultFromEvaluations(List<Double> evaluations) {
        double best = evaluations.get(0);
        double worst = evaluations.get(0);
        double sumOfEvaluations = 0;

        for (Double evaluation: evaluations) {
            sumOfEvaluations += evaluation;

            if(best > evaluation) best = evaluation;
            if (worst < evaluation) worst = evaluation;
        }

        double averageOfEvaluations = sumOfEvaluations / evaluations.size();

        AlgorithmResult result = new AlgorithmResult(best, worst, averageOfEvaluations);
        result.standardDeviation = calculateStandardDeviation(evaluations);
        return result;
    }

    public static double calculateStandardDeviation(List<Double> numbers) {
        double sumOfNumbers = numbers.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double mean = sumOfNumbers / numbers.size();

        double standardDeviation = 0;
        for(Double number: numbers) {
            standardDeviation += Math.pow(number - mean, 2);
        }

        standardDeviation /= numbers.size();

        standardDeviation = Math.sqrt(standardDeviation);

        return standardDeviation;
    }
}
