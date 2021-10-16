package model.city;

import java.util.Objects;

import static model.city.CityType.*;

public class City {

    private final int number;
    private final int xCoordinate;
    private final int yCoordinate;
    private final int demand;
    private CityType cityType;

    public City(int number,
                int xCoordinate,
                int yCoordinate,
                int demand) {
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demand = demand;
        cityType = DELIVERY_CITY;
    }

    public int getNumber() {
        return number;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getDemand() {
        return demand;
    }

    public boolean isDepotCity() {
        return cityType == DEPOT_CITY;
    }

    public boolean isDeliveryCity() {
        return cityType == DELIVERY_CITY;
    }

    public void convertCityTypeToDepot(){
        cityType = DEPOT_CITY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return number == city.number &&
                xCoordinate == city.xCoordinate &&
                yCoordinate == city.yCoordinate &&
                demand == city.demand &&
                cityType == city.cityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, xCoordinate, yCoordinate, demand, cityType);
    }

    @Override
    public String toString() {
        return "City{" +
                "number=" + number +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", demand=" + demand +
                ", cityType=" + cityType +
                '}';
    }
}
