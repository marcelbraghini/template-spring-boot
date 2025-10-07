package template_spring_boot.template.brasilapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import template_spring_boot.template.brasilapi.cache.BrasilApiCache;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.external.BrasilApiClient;
import template_spring_boot.template.testutil.TestFixtures;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrasilApiServiceTest {
    @Mock
    private BrasilApiClient client;

    @Mock
    private BrasilApiCache cache;

    private BrasilApiService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new BrasilApiService(client, cache);
    }

    @Test
    void findByCep_returnsFromCache_whenPresent() {
        final BrasilApiAddress cached = TestFixtures.sampleAddress();
        when(cache.get(TestFixtures.SAMPLE_CEP)).thenReturn(Optional.of(cached));

        final BrasilApiAddress result = service.findByCep(TestFixtures.SAMPLE_CEP);

        assertNotNull(result);
        assertEquals(TestFixtures.SAMPLE_CEP, result.getCep());
        verify(cache, times(1)).get(TestFixtures.SAMPLE_CEP);
        verifyNoInteractions(client);
    }

    @Test
    void findByCep_callsExternalAndUpdatesCache_whenNotInCache() {
        when(cache.get(TestFixtures.SAMPLE_CEP)).thenReturn(Optional.empty());

        ExternalBrasilApiDTO dto = TestFixtures.sampleExternalDto();

        when(client.fetchByCep(TestFixtures.SAMPLE_CEP)).thenReturn(dto);

        final BrasilApiAddress result = service.findByCep(TestFixtures.SAMPLE_CEP);

        assertNotNull(result);
        assertEquals(TestFixtures.SAMPLE_CEP, result.getCep());

        ArgumentCaptor<BrasilApiAddress> captor = ArgumentCaptor.forClass(BrasilApiAddress.class);
        verify(cache).put(eq(TestFixtures.SAMPLE_CEP), captor.capture(), eq(TestFixtures.DEFAULT_TTL));

        BrasilApiAddress cached = captor.getValue();
        assertEquals(TestFixtures.SAMPLE_CEP, cached.getCep());
    }
}
