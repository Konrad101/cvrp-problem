package algorithm.evolutionary.mutation;

import model.SolvedPath;

public interface Mutator {

    SolvedPath mutation(SolvedPath solution);
}
