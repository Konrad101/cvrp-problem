package algorithm;

import model.CvrpData;
import model.SolvedPath;

public interface PathResolverAlgorithm {

    SolvedPath findOptimalPath(CvrpData cvrpData);
}
