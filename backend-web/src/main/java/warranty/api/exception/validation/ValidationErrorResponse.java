package warranty.api.exception.validation;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ValidationErrorResponse {

    private HttpStatus httpStatus;
    private List<MessageError> validationErrors;
    private String path;
    private String api;
    private ZonedDateTime timestamp;

    public ValidationErrorResponse(HttpStatus httpStatus, List<MessageError> validationErrors, String path, String api, ZonedDateTime timestamp) {
        this.httpStatus = httpStatus;
        this.validationErrors = validationErrors;
        this.path = path;
        this.api = api;
        this.timestamp = timestamp;
    }
}
