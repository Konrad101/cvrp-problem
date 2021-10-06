package file.repository;

import file.CvrpFileDataMapper;
import model.CvrpData;

import java.util.List;

import static file.repository.TextFileDataReader.readFileContent;

public class CvrpFileDataRepository implements FileRepository {

    public CvrpFileDataRepository() {
    }

    @Override
    public CvrpData getCvrpData(String filePath) {
        List<String> fileData = readFileContent(filePath);

        CvrpFileDataMapper mapper = new CvrpFileDataMapper(fileData);
        return mapper.mapToCvrpFileData();
    }
}
