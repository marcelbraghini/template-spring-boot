package template_spring_boot.template.brasilapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.external.BrasilApiClient;
import template_spring_boot.template.brasilapi.exceptions.InvalidCepException;

import java.util.regex.Pattern;

@Service
public class BrasilApiService {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiService.class);
    private static final Pattern CEP_PATTERN = Pattern.compile("^(?:[0-9]{8}|[0-9]{5}-[0-9]{3})$");

    private final BrasilApiClient brasilApiClient;

    public BrasilApiService(BrasilApiClient brasilApiClient) {
        this.brasilApiClient = brasilApiClient;
    }

    public BrasilApiAddress findByCep(final String cep) {
        if (cep == null) {
            logger.warn("CEP is null");
            throw new InvalidCepException("CEP inválido ou mal formatado");
        }

        String normalized = cep.replaceAll("[^0-9]", "");

        if (!CEP_PATTERN.matcher(cep).matches() && !CEP_PATTERN.matcher(normalized).matches()) {
            logger.warn("CEP {} is invalid", cep);
            throw new InvalidCepException("CEP inválido ou mal formatado");
        }

        String toQuery = normalized;
        logger.info("Searching for cep {} (normalized {})", cep, toQuery);
        ExternalBrasilApiDTO dto = brasilApiClient.fetchByCep(toQuery);
        BrasilApiAddress addr = new BrasilApiAddress(
                dto.getCep(),
                dto.getState(),
                dto.getCity(),
                dto.getNeighborhood(),
                dto.getStreet(),
                dto.getService()
        );
        return addr;
    }
}

