package warranty.api.exception;

/**
 * This class is used to handle the ProofOfPurchaseNotFoundException exception.
 */
public class ProofOfPurchaseNotFoundException extends RuntimeException {
    /**
     * Constructor for the ProofOfPurchaseNotFoundException class.
     *
     * @param message The error message.
     */
    public ProofOfPurchaseNotFoundException(String message) {
        super(message);
    }
}
