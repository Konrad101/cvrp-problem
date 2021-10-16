package algorithm.greedy;

import algorithm.TestBase;
import model.CvrpData;
import model.SolvedPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static solution.SolvedPathAssertion.assertThatSolvedPath;

class GreedyPathResolverTest extends TestBase {

    @BeforeEach
    void setAlgorithm() {
        pathResolver = new GreedyPathResolver();
    }

    @Test
    void solvedPathIsValidForBasicProblem() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPath(optimalPath)
                .doesNotRepeatDeliveryCity()
                .isPossibleToRideByTruck(cvrpData.getTruck())
                .startsAndEndsWithDepotCity();
    }

    @Test
    void solvedPathIsValidForHardProblem() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(HARD_PROBLEM_FILE_PATH);

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPath(optimalPath)
                .doesNotRepeatDeliveryCity()
                .isPossibleToRideByTruck(cvrpData.getTruck())
                .startsAndEndsWithDepotCity();
    }
}