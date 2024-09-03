package warranty.api.exception;

/**
 * This class is used to handle the ProductNotFoundException exception.
 */
public class ProductNotFoundException extends RuntimeException {
    /**
     * Constructor for the ProductNotFoundException class.
     *
     * @param message The error message.
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
