package launcher.tester;

import algorithm.result.AlgorithmResult;
import algorithm.result.PopulationResult;
import algorithm.result.PopulationResultWithCurrentElement;

import java.util.ArrayList;
import java.util.List;

import static algorithm.result.AlgorithmResult.calculateStandardDeviation;

public class ResultPrinter {

    void printResultWithCurrentElement(List<PopulationResultWithCurrentElement> results) {
        List<PopulationResult> populationResults = new ArrayList<>(results);
        printResultForEvolutionary(populationResults);
    }

    void printResultForEvolutionary(List<PopulationResult> results) {
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

    void printResult(AlgorithmResult algorithmResult) {
        System.out.println("best: " + algorithmResult.getBestElementValue());
        System.out.println("worst: " + algorithmResult.getWorstElementValue());
        System.out.println("average: " + algorithmResult.getAverageElementValue());
        System.out.println("std: " + algorithmResult.getStandardDeviation());
    }
}
