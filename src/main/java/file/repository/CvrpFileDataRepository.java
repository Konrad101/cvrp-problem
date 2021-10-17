package file.repository;

import algorithm.evolutionary.PopulationResult;
import file.repository.reader.CvrpFileDataMapper;
import model.CvrpData;

import java.util.List;
import java.util.stream.Collectors;

import static file.repository.reader.TextFileDataReader.readFileContent;
import static file.repository.writer.CsvFileDataWriter.writeCsvData;

public class CvrpFileDataRepository implements FileRepository {

    public CvrpFileDataRepository() {
    }

    @Override
    public CvrpData getCvrpData(String filePath) {
        List<String> fileData = readFileContent(filePath);

        CvrpFileDataMapper mapper = new CvrpFileDataMapper(fileData);
        return mapper.mapToCvrpFileData();
    }

    @Override
    public void saveEvolutionaryAlgorithmResult(String filePath, List<PopulationResult> populationsResults) {
        List<String[]> fileData = populationsResults.stream()
                .map(result -> {
                    String[] rowData = new String[4];
                    rowData[0] = String.valueOf(result.getPopulationNumber());
                    rowData[1] = String.valueOf(result.getBestElementValue());
                    rowData[2] = String.valueOf(result.getAverageElementValue());
                    rowData[3] = String.valueOf(result.getWorstElementValue());

                    return rowData;
                })
                .collect(Collectors.toList());

        fileData.add(0, getAlgorithmResultsCsvHeader());
        writeCsvData(filePath, fileData);
    }

    private String[] getAlgorithmResultsCsvHeader() {
        String[] headerData = new String[4];
        headerData[0] = "POPULATION_NUMBER";
        headerData[1] = "BEST";
        headerData[2] = "AVG";
        headerData[3] = "WORST";

        return headerData;
    }
}
