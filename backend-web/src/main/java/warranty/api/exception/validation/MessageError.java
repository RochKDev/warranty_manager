package warranty.api.exception.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class is used to handle the MessageError exception.
 */
@Data
@AllArgsConstructor
public class MessageError {

    String field;

    String error;
}