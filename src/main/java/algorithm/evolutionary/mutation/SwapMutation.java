package algorithm.evolutionary.mutation;

import algorithm.reparation.Repairer;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.City;

import java.util.List;
import java.util.Random;

public class SwapMutation implements Mutator {

    private static final Random random = new Random();

    private final Repairer repairer;

    public SwapMutation(Repairer repairer) {
        this.repairer = repairer;
    }

    @Override
    // this method can be void
    public SolvedPath mutation(SolvedPath solution) {

        List<CitiesConnection> connections = solution.getConnections();
        CitiesConnection firstConnection = extractRandomCitiesConnection(connections);
        CitiesConnection secondConnection = extractRandomCitiesConnection(connections);

        // to make sure that both cities are different
        while (firstConnection.equals(secondConnection)
                || firstConnection.getDestinationPlace().equals(secondConnection.getDestinationPlace())) {

            secondConnection = extractRandomCitiesConnection(connections);
        }

        swapDestinationCities(firstConnection, secondConnection);

        return repairer.repairPath(solution);
    }

    private CitiesConnection extractRandomCitiesConnection(List<CitiesConnection> connections) {
        return connections.get(random.nextInt(connections.size()));
    }

    private void swapDestinationCities(CitiesConnection firstConnection, CitiesConnection secondConnection) {
        City firstDestinationPlace = firstConnection.getDestinationPlace();

        firstConnection.setDestinationPlace(secondConnection.getDestinationPlace());
        secondConnection.setDestinationPlace(firstDestinationPlace);
    }
}
