package file.repository;

import algorithm.evolutionary.PopulationResult;
import model.CvrpData;

import java.util.List;

public interface FileRepository {

    CvrpData getCvrpData(String filePath);

    void saveEvolutionaryAlgorithmResult(String filePath, List<PopulationResult> result);

}
