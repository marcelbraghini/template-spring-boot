package template_spring_boot.template.adapters.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import template_spring_boot.template.adapters.client.dto.AddressResponse;
import template_spring_boot.template.application.exceptions.ExternalServiceException;
import template_spring_boot.template.application.exceptions.NotFoundCepException;
import template_spring_boot.template.application.service.gateway.AuditService;

@Component
public class BrasilApiClient {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiClient.class);
    private static final String API_CEP_V_2 = "api/cep/v2/";
    private final RestTemplate restTemplate;
    private final String url;
    private final AuditService auditService;

    @Autowired
    public BrasilApiClient(final RestTemplate restTemplate,
                           final @Value("${brasilapi.url:https://brasilapi.com.br/}") String url,
                           final AuditService auditService) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.auditService = auditService;
    }

    public AddressResponse fetchAddressByCep(String cep) {
        final String base = this.url == null ? "" : this.url.trim();
        final String requestUrl = base + API_CEP_V_2 + cep;

        logger.info("Querying provider {} for cep {}", requestUrl, cep);

        try {
            final ResponseEntity<AddressResponse> externalBrasilApiDTOResponseEntity
                    = restTemplate.getForEntity(requestUrl, AddressResponse.class);

            if (externalBrasilApiDTOResponseEntity.getStatusCode()
                    == HttpStatus.OK && externalBrasilApiDTOResponseEntity.getBody() != null) {
                saveAudit(cep, requestUrl, "SUCCESS", "200 OK");
                return externalBrasilApiDTOResponseEntity.getBody();
            }

        } catch (final HttpClientErrorException.NotFound nf) {
            saveAudit(cep, requestUrl, "NOT_FOUND", nf.getMessage());
            throw new ExternalServiceException("Provider returned 404 for cep: " + cep+ "url=" + requestUrl);
        } catch (final RestClientException ex) {
            saveAudit(cep, requestUrl, "ERROR", ex.getMessage());
            throw new ExternalServiceException("Provider had errors while trying to fetch cep: " + cep);
        }

        logger.error("CEP {} not found in provider {}", cep, requestUrl);
        saveAudit(cep, requestUrl, "NOT_FOUND", "Provider returned non-OK response");
        throw new NotFoundCepException("CEP " + cep + " not found in provider " + requestUrl);
    }

    private void saveAudit(final String cep, final String requestUrl, final String status, final String message) {
        try {
            auditService.auditCep(cep, requestUrl, status, message);
            logger.debug("Saved cep audit for {} status={}", cep, status);
        } catch (final Exception e) {
            logger.error("Failed to save cep audit for {}: {}", cep, e.getMessage());
        }
    }
}
