package warranty.api.exception.validation;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * This class is used to create a custom error response for the API.
 */
@Data
public class ValidationErrorResponse {

    private HttpStatus httpStatus;
    private List<MessageError> validationErrors;
    private String path;
    private String api;
    private ZonedDateTime timestamp;

    /**
     * Constructor for the ValidationErrorResponse class.
     *
     * @param httpStatus       The HTTP status code.
     * @param validationErrors The validation errors.
     * @param path             The path of the API.
     * @param api              The API.
     * @param timestamp        The timestamp of the error.
     */
    public ValidationErrorResponse(HttpStatus httpStatus, List<MessageError> validationErrors, String path, String api, ZonedDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.validationErrors = validationErrors;
        this.path = path;
        this.api = api;
        this.timestamp = timestamp;
    }
}
