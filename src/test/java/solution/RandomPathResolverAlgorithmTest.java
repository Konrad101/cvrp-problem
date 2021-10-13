package solution;

import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithm.random.RandomPathResolverAlgorithm;

import static solution.SolvedPathAssertion.assertThatSolvedPath;

class RandomPathResolverAlgorithmTest {

    private static final String FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";

    private FileRepository fileRepository;
    private RandomPathResolverAlgorithm pathResolver;

    @BeforeEach
    void initialize() {
        fileRepository = new CvrpFileDataRepository();
        pathResolver = new RandomPathResolverAlgorithm();
    }


    @Test
    void solvedPathShouldContainTwoOccurrencesOfDeliveryCity() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(FILE_PATH);

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPath(optimalPath)
                .doesNotRepeatDeliveryCity()
                .isPossibleToRideByTruck(cvrpData.getTruck());
    }
}