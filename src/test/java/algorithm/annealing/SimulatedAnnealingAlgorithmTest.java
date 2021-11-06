package algorithm.annealing;

import algorithm.TestBase;
import algorithm.evolutionary.mutation.InvertMutation;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.mutation.SwapMutation;
import algorithm.reparation.Repairer;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.Test;

class SimulatedAnnealingAlgorithmTest extends TestBase {

    void setUpSimulatedAnnealingAlgorithm(Mutator mutator) {
        pathResolver = new SimulatedAnnealingAlgorithm(mutator);
    }

    @Test
    void solvedPathIsValidForBasicProblemSwapMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpSimulatedAnnealingAlgorithm(new SwapMutation(repairer));

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

        setUpSimulatedAnnealingAlgorithm(new InvertMutation(repairer));

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

        setUpSimulatedAnnealingAlgorithm(new SwapMutation(repairer));

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

        setUpSimulatedAnnealingAlgorithm(new SwapMutation(repairer));

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }
}
