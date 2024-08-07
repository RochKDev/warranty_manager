package warranty.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.ZonedDateTime;

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
}
