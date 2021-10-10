package file.repository;

import file.DataPrefix;
import file.error.InvalidSingleLineValueExtractionError;
import file.error.WrongFileDataException;
import model.CvrpData;
import model.city.DeliveryCitiesMap;
import model.city.City;

import java.util.*;
import java.util.stream.Collectors;

import static file.DataPrefix.*;
import static java.util.stream.IntStream.range;

public class CvrpFileDataMapper {

    private static final int DEFAULT_DEPOT_CITY_NUMBER = 1;
    private static final String DATA_DELIMITER = " : ";

    private final List<String> fileData;

    public CvrpFileDataMapper(List<String> fileData) {
        this.fileData = fileData;
    }

    public CvrpData mapToCvrpFileData() {
        String name = extractName();
        String comment = extractComment();

        int citiesAmount = extractCitiesAmount();
        int capacity = extractCapacity();

        Collection<City> cities = extractCitiesDetails(citiesAmount);
        City depotCity = extractDepotFromCities(cities);
        cities = cities.stream()
                .filter(city -> city.getNumber() != depotCity.getNumber())
                .collect(Collectors.toUnmodifiableList());
        DeliveryCitiesMap deliveryCitiesMap = new DeliveryCitiesMap(cities);

        return new CvrpData(
                name,
                comment,
                citiesAmount,
                capacity,
                deliveryCitiesMap,
                depotCity);
    }

    private City extractDepotFromCities(Collection<City> cities) {
        Optional<City> depot = cities.stream()
                .filter(city -> city.getNumber() == DEFAULT_DEPOT_CITY_NUMBER)
                .findFirst();

        if(depot.isEmpty()) throw new IllegalArgumentException("cities does not contain depot city");

        City depotCity = depot.get();
        depotCity.convertCityTypeToDepot();
        return depotCity;
    }

    private String extractName() {
        return extractValue(NAME);
    }

    private String extractComment() {
        return extractValue(COMMENT);
    }

    private int extractCitiesAmount() {
        return Integer.parseInt(extractValue(DIMENSION));
    }

    private int extractCapacity() {
        return Integer.parseInt(extractValue(CAPACITY));
    }

    private Collection<City> extractCitiesDetails(int citiesAmount) {
        Collection<City> cities = new HashSet<>();
        range(1, citiesAmount + 1)
                .forEach(cityNumber -> cities.add(
                        extractCity(cityNumber)
                ));

        return cities;
    }

    private City extractCity(int cityNumber) {
        String[] coordinates = extractCityDetail(cityNumber, NODE_COORD_SECTION).split(" ");
        String[] demands = extractCityDetail(cityNumber, DEMAND_SECTION).split(" ");

        int xCoordinate = Integer.parseInt(coordinates[2]);
        int yCoordinate = Integer.parseInt(coordinates[3]);
        int demand = Integer.parseInt(demands[1]);

        return new City(cityNumber, xCoordinate, yCoordinate, demand);
    }

    private String extractCityDetail(int cityNumber, DataPrefix dataPrefix) {
        for (int i = 0; i < fileData.size(); i++) {
            if (fileData.get(i).contains(dataPrefix.name())) {
                return fileData.get(i + cityNumber);
            }
        }

        throw new WrongFileDataException(dataPrefix);
    }

    private String extractValue(DataPrefix dataPrefix) {
        Optional<String> value = fileData.stream()
                .filter(line -> line.contains(dataPrefix.name()))
                .map(this::extractSingleLineValue)
                .findFirst();

        if (value.isEmpty()) {
            throw new WrongFileDataException(dataPrefix);
        }
        return value.get();
    }

    private String extractSingleLineValue(String line) {
        String[] fieldNameWithValue = line.split(DATA_DELIMITER);
        if (fieldNameWithValue.length != 2) {
            throw new InvalidSingleLineValueExtractionError(line);
        }
        return fieldNameWithValue[1];
    }
}
