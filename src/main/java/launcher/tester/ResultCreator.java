package launcher.tester;

import algorithm.result.PopulationResult;
import algorithm.result.PopulationResultWithCurrentElement;

import java.util.*;

public class ResultCreator {

    public List<PopulationResultWithCurrentElement> createResultFromAllIterationsSearchResultsWithCurrentElement(
            List<List<PopulationResultWithCurrentElement>> results,
            int algorithmIterations
    ) {
        List<List<PopulationResult>> populationResults = new ArrayList<>();
        results.forEach(
                oneIterationResult -> {
                    List<PopulationResult> populationResult = new ArrayList<>(oneIterationResult);
                    populationResults.add(populationResult);
                });

        Map<Integer, Double> sumOfCurrentForEachPopulation = new HashMap<>();
        results.forEach(
                oneIterationResults -> oneIterationResults
                        .forEach(result -> sumOfCurrentForEachPopulation.put(result.getPopulationNumber(), 0.)));

        results.forEach(
                oneIterationResults -> oneIterationResults.forEach(result -> {
                    int populationNumber = result.getPopulationNumber();
                    Double sumOfCurrents = sumOfCurrentForEachPopulation.get(populationNumber);

                    sumOfCurrentForEachPopulation.put(populationNumber, sumOfCurrents + result.getCurrentElementValue());
                }));

        int populationsAmount = sumOfCurrentForEachPopulation.size();

        // to get avg and worst
        List<PopulationResult> resultFromAllIterationsPopulationResults =
                createResultFromAllIterationsPopulationResults(populationResults, algorithmIterations);

        // to get best
        Map<Integer, Double> bestEvaluationsForEachIteration = getBestEvaluationsForEachIteration(populationResults);

        List<PopulationResultWithCurrentElement> tabuSearchResults = new ArrayList<>();
        for (int populationNumber = 1; populationNumber <= populationsAmount; populationNumber++) {

            Double sumOfCurrentsForCurrentPopulation = sumOfCurrentForEachPopulation.get(populationNumber);
            double averageCurrent = sumOfCurrentsForCurrentPopulation / algorithmIterations;

            PopulationResult populationResult = resultFromAllIterationsPopulationResults.get(populationNumber - 1);
            double averageBest = bestEvaluationsForEachIteration.get(populationNumber);
            double averageWorst = populationResult.getWorstElementValue();
            double averageFromAverages = populationResult.getAverageElementValue();

            tabuSearchResults.add(new PopulationResultWithCurrentElement(
                    populationNumber,
                    averageBest,
                    averageWorst,
                    averageFromAverages,
                    averageCurrent
            ));
        }

        return tabuSearchResults;
    }

    public List<PopulationResult> createResultFromAllIterationsPopulationResults(
            List<List<PopulationResult>> results,
            int algorithmIterations
    ) {
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

        int populationsAmount = sumOfWorstsForEachPopulation.size();

        List<PopulationResult> populationResults = new ArrayList<>();
        for (int populationNumber = 1; populationNumber <= populationsAmount; populationNumber++) {
            Double sumOfBestsForCurrentPopulation = sumOfBestsForEachPopulation.get(populationNumber);
            Double sumOfWorstsForCurrentPopulation = sumOfWorstsForEachPopulation.get(populationNumber);
            Double sumOfAveragesForCurrentPopulation = sumOfAveragesForEachPopulation.get(populationNumber);

            double averageBest = sumOfBestsForCurrentPopulation / algorithmIterations;
            double averageWorst = sumOfWorstsForCurrentPopulation / algorithmIterations;
            double averageFromAverages = sumOfAveragesForCurrentPopulation / algorithmIterations;

            populationResults.add(new PopulationResult(
                    populationNumber,
                    averageBest,
                    averageWorst,
                    averageFromAverages
            ));
        }

        return populationResults;
    }

    private Map<Integer, Double> getBestEvaluationsForEachIteration(List<List<PopulationResult>> results) {
        Map<Integer, Double> bestEvaluationForIteration = new HashMap<>();

        results.forEach(
                oneIterationResults -> oneIterationResults.forEach(result -> {
                    int populationNumber = result.getPopulationNumber();
                    double bestElementValue = result.getBestElementValue();

                    if(bestElementValue < bestEvaluationForIteration.getOrDefault(populationNumber, Double.MAX_VALUE)){
                        bestEvaluationForIteration.put(populationNumber, bestElementValue);
                    }
                }));

        return bestEvaluationForIteration;
    }
}
