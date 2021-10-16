package algorithm.evolutionary;

import algorithm.TestBase;
import algorithm.evolutionary.crossover.CrossoverAlgorithm;
import algorithm.evolutionary.crossover.OrderedCrossover;
import algorithm.evolutionary.mutation.InvertMutation;
import algorithm.evolutionary.mutation.Mutator;
import algorithm.evolutionary.mutation.SwapMutation;
import algorithm.evolutionary.selection.RouletteSelection;
import algorithm.evolutionary.selection.SelectionAlgorithm;
import algorithm.evolutionary.selection.TournamentSelection;
import algorithm.reparation.BasicRepairer;
import algorithm.reparation.Repairer;
import model.CvrpData;
import model.SolvedPath;
import model.truck.Truck;
import org.junit.jupiter.api.Test;

class EvolutionaryAlgorithmTest extends TestBase {

    void setUpEvolutionaryAlgorithm(
            SelectionAlgorithm selectionAlgorithm,
            CrossoverAlgorithm crossoverAlgorithm,
            Mutator mutator,
            Repairer repairer
    ) {
        pathResolver = new EvolutionaryAlgorithm(
                selectionAlgorithm,
                crossoverAlgorithm,
                mutator,
                repairer
        );
    }

    @Test
    void solvedPathIsValidForBasicProblemUsingTournamentSelectionAndOrderedCrossoverAndSwapMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpEvolutionaryAlgorithm(
                new TournamentSelection(),
                new OrderedCrossover(repairer),
                new SwapMutation(repairer),
                repairer
                );

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForBasicProblemUsingRouletteSelection() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpEvolutionaryAlgorithm(
                new RouletteSelection(),
                new OrderedCrossover(repairer),
                new SwapMutation(repairer),
                repairer
        );

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    @Test
    void solvedPathIsValidForBasicProblemUsingInvertMutation() {
        // given
        CvrpData cvrpData = fileRepository.getCvrpData(BASIC_PROBLEM_FILE_PATH);
        Truck truck = cvrpData.getTruck();

        Repairer repairer = getRepairer(cvrpData);

        setUpEvolutionaryAlgorithm(
                new TournamentSelection(),
                new OrderedCrossover(repairer),
                new InvertMutation(repairer),
                repairer
        );

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

        Repairer repairer = getRepairer(cvrpData);

        setUpEvolutionaryAlgorithm(
                new TournamentSelection(),
                new OrderedCrossover(repairer),
                new SwapMutation(repairer),
                repairer
        );

        // when
        SolvedPath optimalPath = pathResolver.findOptimalPath(cvrpData);

        // then
        assertThatSolvedPathIsValid(optimalPath, truck);
    }

    private Repairer getRepairer(CvrpData cvrpData) {
        return new BasicRepairer(
                cvrpData.getDepotCity(),
                cvrpData.getTruck());
    }
}