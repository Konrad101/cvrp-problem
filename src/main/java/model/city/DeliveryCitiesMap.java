package model.city;

import java.util.Collection;

public class DeliveryCitiesMap {

    private final Collection<City> cities;

    public DeliveryCitiesMap(Collection<City> cities) {
        this.cities = cities;
    }

    public Collection<City> getCities() {
        return cities;
    }
}
