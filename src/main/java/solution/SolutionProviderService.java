package solution;

import model.CvrpData;
import model.SolvedPath;
import solution.resolver.PathResolver;

public class SolutionProviderService {

    private final PathResolver resolver;
    private final CvrpData cvrpData;

    public SolutionProviderService(PathResolver resolver,
                                   CvrpData cvrpData) {
        this.resolver = resolver;
        this.cvrpData = cvrpData;
    }

    public SolvedPath getSolution() {
        return resolver.findOptimalPath(cvrpData);
    }
}
