package file.repository;

import model.CvrpData;

public interface FileRepository {

    CvrpData getCvrpData(String filePath);

}
