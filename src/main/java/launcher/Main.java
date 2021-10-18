package launcher;

import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import model.CvrpData;

public class Main {

    private static final String CVRP_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";

    public static void main(String[] args) {
        FileRepository repository = new CvrpFileDataRepository();
        CvrpData cvrpData = repository.getCvrpData(CVRP_PROBLEM_FILE_PATH);
        System.out.println(cvrpData.getComment());

        AlgorithmTester tester = new AlgorithmTester(repository);
        tester.runGreedyAlgorithm(cvrpData);
        tester.runRandomAlgorithm(cvrpData);
        tester.runEvolutionaryAlgorithm(cvrpData);
    }

}
