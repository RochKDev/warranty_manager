package warranty.api.exception;

public class UserEmailUnavailableException extends RuntimeException {
    public UserEmailUnavailableException(String message) {
        super(message);
    }
}
