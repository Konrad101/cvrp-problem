package model;

import model.city.City;
import model.city.DeliveryCitiesMap;
import model.truck.Truck;

import java.util.Objects;

public class CvrpData {
    private final String name;
    private final String comment;

    private final int citiesAmount;
    private final Truck truck;

    private final DeliveryCitiesMap deliveryCitiesMap;
    private final City depotCity;

    public CvrpData(String name,
                    String comment,
                    int citiesAmount,
                    int truckCapacity,
                    DeliveryCitiesMap deliveryCitiesMap,
                    City depotCity) {
        this.name = name;
        this.comment = comment;
        this.citiesAmount = citiesAmount;
        this.truck = new Truck(truckCapacity);
        this.deliveryCitiesMap = deliveryCitiesMap;
        this.depotCity = depotCity;
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

    public Truck getTruck() {
        return truck;
    }

    public DeliveryCitiesMap getDeliveryCitiesMap() {
        return deliveryCitiesMap;
    }

    public City getDepotCity() {
        return depotCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CvrpData that = (CvrpData) o;
        return citiesAmount == that.citiesAmount &&
                truck.equals(that.truck) &&
                Objects.equals(name, that.name) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(deliveryCitiesMap, that.deliveryCitiesMap) &&
                Objects.equals(depotCity, that.depotCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, comment, citiesAmount, truck, deliveryCitiesMap, depotCity);
    }
}
