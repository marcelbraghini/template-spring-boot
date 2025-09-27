package template_spring_boot.template.brasilapi.exceptions;

public class NotFoundCepException extends RuntimeException {
    public NotFoundCepException(String message) {
        super(message);
    }
}

