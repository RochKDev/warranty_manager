package warranty.api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * This class is used to create a custom error response for the API.
 */
@Data
public class ApiErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private String path;
    private String api;
    private ZonedDateTime timestamp;

    /**
     * Constructor for the ApiErrorResponse class.
     * @param httpStatus The HTTP status code.
     * @param message The error message.
     * @param path  The path of the API.
     * @param api The API.
     * @param timestamp The timestamp of the error.
     */
    public ApiErrorResponse(HttpStatus httpStatus, String message, String path, String api, ZonedDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.path = path;
        this.api = api;
        this.timestamp = timestamp;
    }
}