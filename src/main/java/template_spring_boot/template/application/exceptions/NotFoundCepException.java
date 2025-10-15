package template_spring_boot.template.application.exceptions;

public class NotFoundCepException extends RuntimeException {
    public NotFoundCepException(String message) {
        super(message);
    }
}

