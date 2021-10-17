package file.repository.writer;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileDataWriter {

    private static final Logger log = LoggerFactory.getLogger(CsvFileDataWriter.class);

    private CsvFileDataWriter() {
    }

    public static void writeCsvData(String filePath, List<String[]> dataToWrite) {
        File file = new File(filePath);

        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            dataToWrite.forEach(writer::writeNext);

            writer.close();
        } catch (IOException ex) {
            log.error("Unable to write data to file: {}", filePath);
            throw new IllegalArgumentException("Invalid file path: " + ex);
        }
    }
}
