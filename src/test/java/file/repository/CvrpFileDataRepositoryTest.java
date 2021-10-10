package file.repository;

import model.CvrpData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CvrpFileDataRepositoryTest {

    private static final String FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";

    private FileRepository fileRepository;

    @BeforeEach
    void initialiseRepository() {
        fileRepository = new CvrpFileDataRepository();
    }

    @Test
    void shouldReadAndExtractAllFileData() {
        // when
        CvrpData cvrpData = fileRepository.getCvrpData(FILE_PATH);
        // then
        assertThat(cvrpData.getName())
                .isEqualTo("A-n32-k5");
        assertThat(cvrpData.getCitiesAmount())
                .isEqualTo(32);
        assertThat(cvrpData.getDeliveryCitiesMap().getCities().size())
                .isEqualTo(31);
    }
}