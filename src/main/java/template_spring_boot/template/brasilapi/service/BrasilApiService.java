package template_spring_boot.template.brasilapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.external.BrasilApiClient;
import template_spring_boot.template.brasilapi.validation.CepValidator;

@Service
public class BrasilApiService {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiService.class);

    private final BrasilApiClient brasilApiClient;

    public BrasilApiService(final BrasilApiClient brasilApiClient) {
        this.brasilApiClient = brasilApiClient;
    }

    public BrasilApiAddress findByCep(final String cep) {
        final String normalized = CepValidator.normalize(cep);
        CepValidator.validate(cep, normalized);

        final ExternalBrasilApiDTO externalBrasilApiDTO = brasilApiClient.fetchByCep(normalized);

        return new BrasilApiAddress(
                externalBrasilApiDTO.getCep(),
                externalBrasilApiDTO.getState(),
                externalBrasilApiDTO.getCity(),
                externalBrasilApiDTO.getNeighborhood(),
                externalBrasilApiDTO.getStreet(),
                externalBrasilApiDTO.getService()
        );
    }
}
