package warranty.api.exception;

/**
 * This class is used to handle the UserEmailNotFoundException exception.
 */
public class UserEmailNotFoundException extends RuntimeException {
    /**
     * Constructor for the UserEmailNotFoundException class.
     *
     * @param message The error message.
     */
    public UserEmailNotFoundException(String message) {
        super(message);
    }
}