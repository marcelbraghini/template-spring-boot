package template_spring_boot.template.application.service.gateway;

public interface AuditService {
    void auditCep(final String cep, final String requestUrl, final String status, final String message);
}

