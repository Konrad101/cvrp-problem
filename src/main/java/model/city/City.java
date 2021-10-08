package model.city;

import java.util.Objects;

public class City {

    private final int number;
    private final int xCoordinate;
    private final int yCoordinate;

    private final int demand;

    public City(int number,
                int xCoordinate,
                int yCoordinate,
                int demand) {
        this.number = number;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.demand = demand;
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

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return number == city.number &&
                xCoordinate == city.xCoordinate &&
                yCoordinate == city.yCoordinate &&
                demand == city.demand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, xCoordinate, yCoordinate, demand);
    }

    @Override
    public String toString() {
        return "City{ " +
                "number=" + number +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", demand=" + demand +
                " }";
    }
}
