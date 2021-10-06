package file;

import file.error.InvalidSingleLineValueExtractionError;
import file.error.WrongFileDataException;
import model.CvrpData;
import model.city.CitiesMap;
import model.city.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static file.DataPrefix.*;
import static java.util.stream.IntStream.range;

public class CvrpFileDataMapper {

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

        CitiesMap citiesMap = extractCitiesDetails(citiesAmount);

        return new CvrpData(
                name,
                comment,
                citiesAmount,
                capacity,
                citiesMap);
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

    private CitiesMap extractCitiesDetails(int citiesAmount) {
        List<City> cities = new ArrayList<>();
        range(1, citiesAmount + 1)
                .forEach(cityNumber -> cities.add(
                        extractCity(cityNumber)
                ));

        return new CitiesMap(cities);
    }

    private City extractCity(int cityNumber) {
        String[] coordinates = extractCityDetail(cityNumber, NODE_COORD_SECTION).split(" ");
        String[] demands = extractCityDetail(cityNumber, DEMAND_SECTION).split(" ");

        int xCoordinate = Integer.parseInt(coordinates[2]);
        int yCoordinate = Integer.parseInt(coordinates[3]);
        int demand = Integer.parseInt(demands[1]);

        return new City(xCoordinate, yCoordinate, demand);
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
