package template_spring_boot.template.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import template_spring_boot.template.adapters.database.postgresql.entities.CepAudit;
import template_spring_boot.template.adapters.database.postgresql.repositories.CepAuditRepository;
import template_spring_boot.template.application.service.gateway.AuditService;

import java.time.Instant;

@Service
public class AuditServiceImpl implements AuditService {
    private static final Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);

    private final CepAuditRepository repository;

    public AuditServiceImpl(final CepAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void auditCep(final String cep, final String requestUrl, final String status, final String message) {
        try {
            final CepAudit audit = new CepAudit(cep, requestUrl, status, message, Instant.now());
            repository.save(audit);
            logger.debug("Saved cep audit for {} status={}", cep, status);
        } catch (Exception ex) {
            logger.error("Failed to persist audit for cep {}: {}", cep, ex.getMessage());
        }
    }
}

