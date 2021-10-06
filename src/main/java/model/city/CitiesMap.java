package model.city;

import java.util.List;

public class CitiesMap {

    private final List<City> cities;

    public CitiesMap(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }
}
