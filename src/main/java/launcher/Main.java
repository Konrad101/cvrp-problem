package launcher;

import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;

public class Main {

    private static final String CVRP_PROBLEM_BASIC_PATH = "src\\main\\resources\\dataset\\basic\\";
    private static final String CVRP_PROBLEM_HARD_PATH = "src\\main\\resources\\dataset\\hard\\";

    private static final String FILE_NAME = "A-n60-k9.vrp";

    public static void main(String[] args) {
        FileRepository repository = new CvrpFileDataRepository();
        CvrpData cvrpData = repository.getCvrpData(CVRP_PROBLEM_HARD_PATH + FILE_NAME);
        System.out.println(cvrpData.getComment());

        AlgorithmTester tester = new AlgorithmTester(repository);
        tester.runGreedyAlgorithm(cvrpData);
        tester.runRandomAlgorithm(cvrpData);
        tester.runEvolutionaryAlgorithm(cvrpData);
    }

}
