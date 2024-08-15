package warranty.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import warranty.api.exception.validation.MessageError;
import warranty.api.exception.validation.ValidationErrorResponse;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
