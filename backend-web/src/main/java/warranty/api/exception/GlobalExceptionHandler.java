package warranty.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import warranty.api.exception.validation.MessageError;
import warranty.api.exception.validation.ValidationErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to handle exceptions globally.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is used to handle the ProductNotFoundException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ProductNotFoundException.class)
    public ApiErrorResponse handleCustomerNotFoundException(ProductNotFoundException ex,
                                                            HttpServletRequest request,
                                                            HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the ProofOfPurchaseNotFoundException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ProofOfPurchaseNotFoundException.class)
    public ApiErrorResponse handleProofOfPurchaseNotFound(ProofOfPurchaseNotFoundException ex,
                                                          HttpServletRequest request,
                                                          HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the ProofOfPurchaseConflictException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = ProofOfPurchaseConflictException.class)
    public ApiErrorResponse handleProofOfPurchaseConflict(ProofOfPurchaseConflictException ex,
                                                          HttpServletRequest request,
                                                          HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the UserEmailUnavailableException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = UserEmailUnavailableException.class)
    public ApiErrorResponse handleUserEmailUnavailable(UserEmailUnavailableException ex,
                                                       HttpServletRequest request,
                                                       HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the UserEmailNotFoundException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserEmailNotFoundException.class)
    public ApiErrorResponse handleUserEmailNotFound(UserEmailNotFoundException ex,
                                                    HttpServletRequest request,
                                                    HandlerMethod method) {

        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the UnauthorizedResourceAccess exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ApiErrorResponse.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = UnauthorizedResourceAccess.class)
    public ApiErrorResponse handleUnauthorizedResourceAccess(UnauthorizedResourceAccess ex,
                                                             HttpServletRequest request,
                                                             HandlerMethod method) {
        return new ApiErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }


    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ApiErrorResponse handleFileUploadSizeExceeded(MaxUploadSizeExceededException ex,
                                                             HttpServletRequest request) {
        return new ApiErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                ex.getMessage(),
                request.getRequestURI(),
                "",
                ZonedDateTime.now()
        );
    }

    /**
     * This method is used to handle the MethodArgumentNotValidException exception.
     *
     * @param ex      The exception.
     * @param request The request.
     * @param method  The method.
     * @return The ValidationErrorResponse.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex,
                                                              HttpServletRequest request,
                                                              HandlerMethod method) {
        List<MessageError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new MessageError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST,
                validationErrors,
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now()
        );
    }
}
