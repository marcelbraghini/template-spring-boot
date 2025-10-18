package template_spring_boot.template.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import template_spring_boot.template.adapters.client.RedisClient;
import template_spring_boot.template.adapters.client.dto.AddressResponse;
import template_spring_boot.template.application.service.gateway.AuditService;
import template_spring_boot.template.domain.Address;
import template_spring_boot.template.adapters.client.BrasilApiClient;
import template_spring_boot.template.fixture.TestFixtures;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrasilApiServiceTest {

    @Mock
    private BrasilApiClient client;

    @Mock
    private RedisClient cache;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private BrasilApiService service;

    @Test
    void findAddressByCep_returnsFromCache_whenPresent() {
        final Address cached = TestFixtures.sampleAddress();
        when(cache.get(TestFixtures.SAMPLE_CEP)).thenReturn(Optional.of(cached));

        final Address result = service.findAddressByCep(TestFixtures.SAMPLE_CEP);

        assertNotNull(result);
        assertEquals(TestFixtures.SAMPLE_CEP, result.getCep());
        verify(cache, times(1)).get(TestFixtures.SAMPLE_CEP);
        verifyNoInteractions(client);
    }

    @Test
    void findAddressByCep_callsExternalAndUpdatesCache_whenNotInCache() {
        when(cache.get(TestFixtures.SAMPLE_CEP)).thenReturn(Optional.empty());

        AddressResponse dto = TestFixtures.sampleExternalDto();

        when(client.fetchAddressByCep(TestFixtures.SAMPLE_CEP)).thenReturn(dto);

        final Address result = service.findAddressByCep(TestFixtures.SAMPLE_CEP);

        assertNotNull(result);
        assertEquals(TestFixtures.SAMPLE_CEP, result.getCep());

        ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
        verify(cache).put(eq(TestFixtures.SAMPLE_CEP), captor.capture(), eq(TestFixtures.DEFAULT_TTL));

        Address cached = captor.getValue();
        assertEquals(TestFixtures.SAMPLE_CEP, cached.getCep());
    }
}
