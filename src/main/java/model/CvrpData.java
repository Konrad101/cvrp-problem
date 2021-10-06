package model;

import model.city.CitiesMap;

import java.util.Objects;

public class CvrpData {
    private final String name;
    private final String comment;

    private final int citiesAmount;
    private final int truckCapacity;

    private final CitiesMap citiesMap;

    public CvrpData(String name,
                    String comment,
                    int citiesAmount,
                    int truckCapacity,
                    CitiesMap citiesMap) {
        this.name = name;
        this.comment = comment;
        this.citiesAmount = citiesAmount;
        this.truckCapacity = truckCapacity;
        this.citiesMap = citiesMap;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public int getCitiesAmount() {
        return citiesAmount;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public CitiesMap getCitiesMap() {
        return citiesMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CvrpData that = (CvrpData) o;
        return citiesAmount == that.citiesAmount && truckCapacity == that.truckCapacity && Objects.equals(name, that.name) && Objects.equals(comment, that.comment) && Objects.equals(citiesMap, that.citiesMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, comment, citiesAmount, truckCapacity, citiesMap);
    }
}
