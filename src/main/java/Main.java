import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;
import model.SolvedPath;
import solution.SolutionPresenter;
import solution.SolutionProviderService;
import algorithm.PathResolverAlgorithm;
import algorithm.random.RandomPathResolverAlgorithm;

public class Main {

    private static final String CVRP_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";

    public static void main(String[] args) {
        FileRepository repository = new CvrpFileDataRepository();
        CvrpData cvrpData = repository.getCvrpData(CVRP_PROBLEM_FILE_PATH);

        PathResolverAlgorithm resolver = new RandomPathResolverAlgorithm();
        SolutionProviderService providerService = new SolutionProviderService(resolver, cvrpData);

        SolvedPath solution = providerService.getSolution();
        SolutionPresenter solutionPresenter = new SolutionPresenter(solution, cvrpData);
        solutionPresenter.printSolution();
        solutionPresenter.printEvaluation();

    }
}
