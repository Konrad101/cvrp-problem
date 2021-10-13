package algorithm.evolutionary.selection;

import model.SolvedPath;

import java.util.List;

public interface SelectionAlgorithm {

    SolvedPath selectFromPopulation(List<SolvedPath> population);
}
