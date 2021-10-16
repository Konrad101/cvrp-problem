package model.city.connection;

import model.city.City;

import java.util.Objects;

public class CitiesConnection {
    private final City originPlace;
    private City destinationPlace;

    public CitiesConnection(City originPlace, City destinationPlace) {
        this.originPlace = originPlace;
        this.destinationPlace = destinationPlace;
    }

    public City getOriginPlace() {
        return originPlace;
    }

    public City getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(City destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public boolean containsCity(City city) {
        return originPlace.equals(city) || destinationPlace.equals(city);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CitiesConnection that = (CitiesConnection) o;
        return Objects.equals(originPlace, that.originPlace) && Objects.equals(destinationPlace, that.destinationPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originPlace, destinationPlace);
    }

    @Override
    public String toString() {
        return "CitiesConnection{" +
                "originPlace=" + originPlace +
                ", destinationPlace=" + destinationPlace +
                '}';
    }
}
