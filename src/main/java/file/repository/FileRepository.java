package file.repository;

import algorithm.result.PopulationResult;
import algorithm.result.TabuSearchPopulationResult;
import model.CvrpData;

import java.util.List;

public interface FileRepository {

    CvrpData getCvrpData(String filePath);

    void saveEvolutionaryAlgorithmResult(String filePath, List<PopulationResult> result);

    void saveTabuSearchAlgorithmResult(String filePath, List<TabuSearchPopulationResult> result);

}
