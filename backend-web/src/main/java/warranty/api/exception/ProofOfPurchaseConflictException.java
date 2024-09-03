package warranty.api.exception;

/**
 * This class is used to handle the ProofOfPurchaseConflictException exception.
 */
public class ProofOfPurchaseConflictException extends RuntimeException {
    /**
     * Constructor for the ProofOfPurchaseConflictException class.
     *
     * @param message The error message.
     */
    public ProofOfPurchaseConflictException(String message) {
        super(message);
    }
}
