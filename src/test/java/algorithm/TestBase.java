package algorithm;

import file.repository.CvrpFileDataRepository;
import file.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;

public abstract class TestBase {

    protected static final String BASIC_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\basic\\A-n32-k5.vrp";
    protected static final String HARD_PROBLEM_FILE_PATH = "src\\main\\resources\\dataset\\hard\\A-n54-k7.vrp";

    protected FileRepository fileRepository;
    protected PathResolverAlgorithm pathResolver;

    @BeforeEach
    void initialize() {
        fileRepository = new CvrpFileDataRepository();
    }
}
