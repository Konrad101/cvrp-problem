package algorithm.evolutionary.crossover;

import model.SolvedPath;

public interface CrossoverAlgorithm {

    SolvedPath crossover(SolvedPath firstParent, SolvedPath secondParent);
}
