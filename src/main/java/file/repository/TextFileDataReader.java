package file.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class TextFileDataReader {

    private static final Logger log = LoggerFactory.getLogger(TextFileDataReader.class);

    private TextFileDataReader() {
    }

    public static List<String> readFileContent(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.readAllLines(path);
        } catch (IOException ex) {
            log.error("Unable to read data from file: {}", filePath);
            throw new IllegalArgumentException("Invalid file path: " + ex);
        }
    }

}
