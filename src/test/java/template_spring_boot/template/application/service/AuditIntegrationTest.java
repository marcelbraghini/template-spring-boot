package template_spring_boot.template.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import template_spring_boot.template.adapters.database.postgresql.entities.CepAudit;
import template_spring_boot.template.adapters.database.postgresql.repositories.CepAuditRepository;
import template_spring_boot.template.fixture.TestFixtures;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditIntegrationTest {

    @Mock
    private CepAuditRepository repo;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Captor
    private ArgumentCaptor<CepAudit> captor;

    @Test
    void audit_is_persisted_on_external_call_success() {
        String cep = TestFixtures.SAMPLE_CEP;
        String url = "https://brasilapi.com.br/api/cep/v2/" + cep;
        String status = "OK";
        String message = "served from external";

        auditService.auditCep(cep, url, status, message);

        verify(repo, times(1)).save(captor.capture());
        CepAudit saved = captor.getValue();
        assertThat(saved.getCep()).isEqualTo(cep);
        assertThat(saved.getRequestUrl()).isEqualTo(url);
        assertThat(saved.getStatus()).isEqualTo(status);
        assertThat(saved.getMessage()).isEqualTo(message);
        assertThat(saved.getCreatedAt()).isBeforeOrEqualTo(Instant.now());
    }
}
