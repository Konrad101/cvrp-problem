package algorithm;

import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static solution.SolvedPathAssertion.assertThatSolvedPath;

public abstract class TestBase {

    protected static final String BASIC_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";
    protected static final String HARD_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\hard\\A-n54-k7.vrp";

    protected FileRepository fileRepository;
    protected PathResolverAlgorithm pathResolver;

    @BeforeEach
    void initialize() {
        fileRepository = new CvrpFileDataRepository();
    }

    protected void assertThatSolvedPathIsValid(SolvedPath solvedPath, Truck truck) {
        assertThatSolvedPath(solvedPath)
                .doesNotRepeatDeliveryCity()
                .doesNotRepeatDepotCityAfterDepotCity()
                .doesNotSkipCities()
                .isPossibleToRideByTruck(truck)
                .startsAndEndsWithDepotCity();
    }
}
