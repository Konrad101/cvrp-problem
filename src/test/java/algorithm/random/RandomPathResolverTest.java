package algorithm.random;

import algorithm.TestBase;
import algorithm.reparation.BasicRepairer;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.Test;


class RandomPathResolverTest extends TestBase {

    void setUpRandomAlgorithm(CvrpData cvrpData) {
        BasicRepairer repairer = new BasicRepairer(
                cvrpData.getDepotCity(),
                cvrpData.getTruck());

        pathResolver = new RandomPathResolver(repairer);
    }

    @Test
    void solvedPathIsValid() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        setUpRandomAlgorithm(cvrpData);

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

        setUpRandomAlgorithm(cvrpData);

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }
}