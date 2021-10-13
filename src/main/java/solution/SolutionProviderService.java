package solution;

import model.CvrpData;
import model.SolvedPath;
import algorithm.PathResolverAlgorithm;

public class SolutionProviderService {

    private final PathResolverAlgorithm resolver;
    private final CvrpData cvrpData;

    public SolutionProviderService(PathResolverAlgorithm resolver,
                                   CvrpData cvrpData) {
        this.resolver = resolver;
        this.cvrpData = cvrpData;
    }

    public SolvedPath getSolution() {
        return resolver.findOptimalPath(cvrpData);
    }
}
