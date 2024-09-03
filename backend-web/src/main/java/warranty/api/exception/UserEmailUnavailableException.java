package warranty.api.exception;

/**
 * This class is used to handle the UserEmailUnavailableException exception.
 */
public class UserEmailUnavailableException extends RuntimeException {
    /**
     * Constructor for the UserEmailUnavailableException class.
     *
     * @param message The error message.
     */
    public UserEmailUnavailableException(String message) {
        super(message);
    }
}
