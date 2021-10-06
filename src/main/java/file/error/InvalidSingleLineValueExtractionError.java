package file.error;

public class InvalidSingleLineValueExtractionError extends RuntimeException {

    public InvalidSingleLineValueExtractionError(String invalidValue)
    {
        super("Invalid single line value format, it should be in format 'name : value', but instead was: '" + invalidValue + "'");
    }
}
