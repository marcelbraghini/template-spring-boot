package template_spring_boot.template.application.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import template_spring_boot.template.application.exceptions.dto.ErrorResponse;
import template_spring_boot.template.application.exceptions.ExternalServiceException;
import template_spring_boot.template.application.exceptions.InvalidCepException;
import template_spring_boot.template.application.exceptions.NotFoundCepException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidCep_returnsBadRequest() {
        final InvalidCepException ex = new InvalidCepException("CEP inválido");

        ResponseEntity<ErrorResponse> resp = handler.handleInvalidCep(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getMessage().contains("CEP inválido"));
    }

    @Test
    void handleNotFound_returnsNotFound() {
        final NotFoundCepException ex = new NotFoundCepException("CEP não encontrado");

        ResponseEntity<ErrorResponse> resp = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getMessage().contains("CEP não encontrado"));
    }

    @Test
    void handleExternalService_returnsServerError() {
        final ExternalServiceException ex = new ExternalServiceException("Erro externo");

        ResponseEntity<ErrorResponse> resp = handler.handleExternalService(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getMessage().contains("Erro externo"));
    }
}

