package warranty.api.exception;

/**
 * This class is used to handle the UnauthorizedResourceAccess exception.
 */
public class UnauthorizedResourceAccess extends RuntimeException {
    /**
     * Constructor for the UnauthorizedResourceAccess class.
     *
     * @param message The error message.
     */
    public UnauthorizedResourceAccess(String message) {
        super(message);
    }
}
