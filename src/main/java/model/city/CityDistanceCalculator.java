package model.city;

import java.awt.geom.Point2D;

public class CityDistanceCalculator {

    private CityDistanceCalculator() {
    }

    public static double calculateDistance(CitiesConnection connection) {
        return calculateDistance(
                connection.getOriginPlace(),
                connection.getDestinationPlace());
    }

    public static double calculateDistance(City originPlace, City destinationPlace) {
        return Point2D.distance(
                originPlace.getXCoordinate(),
                originPlace.getYCoordinate(),
                destinationPlace.getXCoordinate(),
                destinationPlace.getYCoordinate()
        );
    }
}
