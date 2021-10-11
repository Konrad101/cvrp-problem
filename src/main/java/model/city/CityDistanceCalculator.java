package model.city;

import java.awt.geom.Point2D;

public class CityDistanceCalculator {

    private CityDistanceCalculator() {
    }

    public static double calculateDistance(CitiesConnection connection) {
        City originPlace = connection.getOriginPlace();
        City destinationPlace = connection.getDestinationPlace();
        return Point2D.distance(
                originPlace.getXCoordinate(),
                originPlace.getYCoordinate(),
                destinationPlace.getXCoordinate(),
                destinationPlace.getYCoordinate()
        );
    }
}
