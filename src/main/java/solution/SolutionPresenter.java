package solution;

import model.CvrpData;
import model.SolvedPath;
import model.city.CitiesConnection;
import model.city.City;
import model.city.CityType;
import model.truck.Truck;

import java.util.List;

public class SolutionPresenter {

    private final SolvedPath solvedPath;
    private final CvrpData cvrpData;

    public SolutionPresenter(SolvedPath solvedPath, CvrpData cvrpData) {
        this.solvedPath = solvedPath;
        this.cvrpData = cvrpData;
    }

    public void printSolution() {
        List<CitiesConnection> connections = solvedPath.getConnections();
        Truck truck = new Truck(cvrpData.getTruck().getMaxCapacity());

        int cityChronologicalNumber = 1;
        for (CitiesConnection connection : connections) {
            City visitedPlace = connection.getOriginPlace();

            if (visitedPlace.getCityType() == CityType.DEPOT_CITY) {
                truck.load();
            }
            truck.unload(visitedPlace.getDemand());

            printCityLine(visitedPlace, truck, cityChronologicalNumber);
            cityChronologicalNumber++;
        }

        City lastDestination = connections.get(connections.size() - 1).getDestinationPlace();
        truck.unload(lastDestination.getDemand());
        printCityLine(lastDestination, truck, cityChronologicalNumber);
    }

    private void printCityLine(City city, Truck truck, int cityChronologicalNumber) {
        System.out.println(cityChronologicalNumber + ". " + city + ", " + truck);
    }
}
