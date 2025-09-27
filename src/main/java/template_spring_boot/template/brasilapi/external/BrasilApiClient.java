package template_spring_boot.template.brasilapi.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.exceptions.ExternalServiceException;
import template_spring_boot.template.brasilapi.exceptions.NotFoundCepException;

import java.util.Arrays;
import java.util.List;

@Component
public class BrasilApiClient {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiClient.class);

    private final RestTemplate restTemplate;
    private final List<String> providers;

    public BrasilApiClient(RestTemplate restTemplate,
                           @Value("${brasilapi.providers:https://brasilapi.com.br/api/cep/v2}") String providersCsv) {
        this.restTemplate = restTemplate;
        this.providers = Arrays.stream(providersCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public ExternalBrasilApiDTO fetchByCep(String cep) {
        boolean hadProviderError = false;
        for (String base : providers) {
            String url = base;
            if (!base.endsWith("/")) {
                url = base + "/";
            }
            url = url + cep;
            logger.info("Trying provider {} for cep {}", base, cep);
            try {
                ResponseEntity<ExternalBrasilApiDTO> resp = restTemplate.getForEntity(url, ExternalBrasilApiDTO.class);
                if (resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null) {
                    logger.info("Provider {} returned result for cep {}", base, cep);
                    return resp.getBody();
                }
            } catch (HttpClientErrorException.NotFound nf) {
                logger.warn("Provider {} returned 404 for cep {}", base, cep);
                // try next provider
            } catch (RestClientException ex) {
                hadProviderError = true;
                logger.error("Provider {} failed for cep {}: {}", base, cep, ex.getMessage());
                // continue to next provider
            }
        }
        if (hadProviderError) {
            logger.warn("Providers had errors while trying to fetch cep {}", cep);
            throw new ExternalServiceException("Error while calling external providers");
        }
        logger.warn("CEP {} not found in any provider", cep);
        throw new NotFoundCepException("CEP not found in any provider");
    }
}
