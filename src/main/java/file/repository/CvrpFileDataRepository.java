package file.repository;

import algorithm.result.PopulationResult;
import algorithm.result.TabuSearchPopulationResult;
import file.repository.reader.CvrpFileDataMapper;
import model.CvrpData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static file.repository.reader.TextFileDataReader.readFileContent;
import static file.repository.writer.CsvFileDataWriter.writeCsvData;

public class CvrpFileDataRepository implements FileRepository {


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

        List<String> algorithmResultsCsvHeader = getAlgorithmResultsCsvHeader();
        fileData.add(0, convertListToArray(algorithmResultsCsvHeader));
        writeCsvData(filePath, fileData);
    }

    @Override
    public void saveTabuSearchAlgorithmResult(String filePath, List<TabuSearchPopulationResult> iterationsResults) {
        List<String[]> fileData = iterationsResults.stream()
                .map(result -> {
                    String[] rowData = new String[5];
                    rowData[0] = String.valueOf(result.getPopulationNumber());
                    rowData[1] = String.valueOf(result.getBestElementValue());
                    rowData[2] = String.valueOf(result.getAverageElementValue());
                    rowData[3] = String.valueOf(result.getWorstElementValue());
                    rowData[4] = String.valueOf(result.getCurrentElementValue());

                    return rowData;
                })
                .collect(Collectors.toList());

        List<String> algorithmResultsCsvHeader = getTabuSearchAlgorithmResultsCsvHeader();
        fileData.add(0, convertListToArray(algorithmResultsCsvHeader));
        writeCsvData(filePath, fileData);
    }

    private List<String> getAlgorithmResultsCsvHeader() {
        List<String> headerData = new ArrayList<>();
        headerData.add("POPULATION_NUMBER");
        headerData.add("BEST");
        headerData.add("AVG");
        headerData.add("WORST");

        return headerData;
    }

    private List<String> getTabuSearchAlgorithmResultsCsvHeader() {
        List<String> algorithmResultsCsvHeader = getAlgorithmResultsCsvHeader();
        algorithmResultsCsvHeader.add("CURRENT");

        return algorithmResultsCsvHeader;
    }

    private String[] convertListToArray(List<String> list) {
        String[] array = new String[list.size()];

        IntStream.range(0, list.size())
                .forEach(i -> array[i] = list.get(i));

        return array;
    }

}
