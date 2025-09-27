package template_spring_boot.template.brasilapi.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import template_spring_boot.template.brasilapi.dto.ErrorResponse;
import template_spring_boot.template.brasilapi.exceptions.ExternalServiceException;
import template_spring_boot.template.brasilapi.exceptions.InvalidCepException;
import template_spring_boot.template.brasilapi.exceptions.NotFoundCepException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCep(InvalidCepException ex) {
        logger.warn("Invalid CEP: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(ex.getMessage(), "InvalidCep", "BadRequest");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFoundCepException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundCepException ex) {
        logger.info("CEP not found: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(ex.getMessage(), "NotFoundCep", "NotFound");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalService(ExternalServiceException ex) {
        logger.error("External service error: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse(ex.getMessage(), "ExternalServiceError", "ExternalError");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponse body = new ErrorResponse("Internal server error", "InternalError", "ServerError");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

