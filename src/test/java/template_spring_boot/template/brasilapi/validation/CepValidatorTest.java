package template_spring_boot.template.brasilapi.validation;

import org.junit.jupiter.api.Test;
import template_spring_boot.template.brasilapi.exceptions.InvalidCepException;

import static org.junit.jupiter.api.Assertions.*;

class CepValidatorTest {
    @Test
    void normalize_removesNonDigits() {
        final String normalized = CepValidator.normalize("12345-678");
        assertEquals("12345678", normalized);
    }

    @Test
    void normalize_null_throws() {
        assertThrows(InvalidCepException.class, () -> CepValidator.normalize(null));
    }

    @Test
    void validate_acceptsValidFormats() {
        CepValidator.validate("12345-678", "12345678");
        CepValidator.validate("12345678", "12345678");
    }

    @Test
    void validate_invalid_throws() {
        assertThrows(InvalidCepException.class, () -> CepValidator.validate("abc", ""));
    }
}
