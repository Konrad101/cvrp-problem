package algorithm.evolutionary.selection;

import model.SolvedPath;

import java.util.Collection;

public interface SelectionAlgorithm {

    SolvedPath selectFromPopulation(Collection<SolvedPath> population);
}
