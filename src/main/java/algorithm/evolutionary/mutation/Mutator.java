package algorithm.evolutionary.mutation;

import model.SolvedPath;

public interface Mutator {

    void mutation(SolvedPath solution, double probability);
}
