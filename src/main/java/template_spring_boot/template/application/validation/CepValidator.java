package template_spring_boot.template.application.validation;

import template_spring_boot.template.application.exceptions.InvalidCepException;

import java.util.regex.Pattern;

public final class CepValidator {
    private static final Pattern CEP_PATTERN = Pattern.compile("^(?:[0-9]{8}|[0-9]{5}-[0-9]{3})$");

    private CepValidator() {
    }

    public static String normalize(final String cep) {
        if (cep == null) {
            throw new InvalidCepException("CEP inválido ou mal formatado");
        }
        return cep.replaceAll("[^0-9]", "");
    }

    public static void validate(final String cep, final String normalized) {
        if (!CEP_PATTERN.matcher(cep).matches() && !CEP_PATTERN.matcher(normalized).matches()) {
            throw new InvalidCepException("CEP inválido ou mal formatado");
        }
    }
}

