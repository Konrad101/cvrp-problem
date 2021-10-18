package algorithm.evolutionary;

import algorithm.result.AlgorithmResult;

public class PopulationResult extends AlgorithmResult {

    private final int populationNumber;

    public PopulationResult(
            int populationNumber,
            double bestElementValue,
            double worstElementValue,
            double averageElementValue) {
        super(bestElementValue, worstElementValue, averageElementValue);
        this.populationNumber = populationNumber;
    }

    public int getPopulationNumber() {
        return populationNumber;
    }
}
