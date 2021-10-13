package algorithm.evolutionary;

public class PopulationResult {

    private final double bestElementValue;
    private final double worstElementValue;
    private final double averageElementValue;

    public PopulationResult(double bestElementValue, double worstElementValue, double averageElementValue) {
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
}
