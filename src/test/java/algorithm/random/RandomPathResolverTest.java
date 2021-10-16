package algorithm.random;

import algorithm.TestBase;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RandomPathResolverTest extends TestBase {

    @BeforeEach
    void setAlgorithm() {
        pathResolver = new RandomPathResolver();
    }

    @Test
    void solvedPathIsValid() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForHardProblem() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(HARD_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }
}