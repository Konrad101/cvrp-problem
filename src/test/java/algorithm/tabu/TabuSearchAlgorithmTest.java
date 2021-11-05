package algorithm.tabu;

import algorithm.TestBase;
import algorithm.evolutionary.mutation.InvertMutation;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.mutation.SwapMutation;
import algorithm.reparation.Repairer;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.Test;

class TabuSearchAlgorithmTest extends TestBase {

    void setUpTabuSearchAlgorithm(Mutator mutator) {
        pathResolver = new TabuSearchAlgorithm(mutator);
    }

    @Test
    void solvedPathIsValidForBasicProblemSwapMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpTabuSearchAlgorithm(new SwapMutation(repairer));

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForBasicProblemInvertMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpTabuSearchAlgorithm(new InvertMutation(repairer));

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForHardProblemSwapMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(HARD_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpTabuSearchAlgorithm(new SwapMutation(repairer));

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForHardProblemInvertMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(HARD_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpTabuSearchAlgorithm(new InvertMutation(repairer));

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }
}
