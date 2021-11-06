package file.repository;

import algorithm.result.PopulationResult;
import algorithm.result.PopulationResultWithCurrentElement;
import model.CvrpData;

import java.util.List;

public interface FileRepository {

    CvrpData getCvrpData(String filePath);

    void saveEvolutionaryAlgorithmResult(String filePath, List<PopulationResult> result);

    void saveAlgorithmResultWithCurrentElement(String filePath, List<PopulationResultWithCurrentElement> result);

}
