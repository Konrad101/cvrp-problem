package algorithm.result;

public class TabuSearchPopulationResult extends PopulationResult {

    private final double currentElementValue;

    public TabuSearchPopulationResult(
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
}
