package template_spring_boot.template.brasilapi.exceptions;

public class InvalidCepException extends RuntimeException {
    public InvalidCepException(String message) {
        super(message);
    }
}

