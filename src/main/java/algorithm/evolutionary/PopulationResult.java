package algorithm.evolutionary;

public class PopulationResult {

    private final int populationNumber;
    private final double bestElementValue;
    private final double averageElementValue;
    private final double worstElementValue;

    public PopulationResult(
            int populationNumber,
            double bestElementValue,
            double worstElementValue,
            double averageElementValue) {
        this.populationNumber = populationNumber;
        this.bestElementValue = bestElementValue;
        this.worstElementValue = worstElementValue;
        this.averageElementValue = averageElementValue;
    }

    public int getPopulationNumber() {
        return populationNumber;
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
}
