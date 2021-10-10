package pathFinder;

import model.CvrpData;
import model.SolvedPath;

public interface PathResolver {
    SolvedPath findOptimalPath(CvrpData cvrpData);
}
