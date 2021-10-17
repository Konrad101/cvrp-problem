package file.error;

import file.repository.reader.DataPrefix;

public class WrongFileDataException extends RuntimeException {

    public WrongFileDataException(DataPrefix dataPrefix) {
        super("Could not find value for prefix: '" + dataPrefix + "'");
    }
}
