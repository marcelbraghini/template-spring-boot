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

import java.time.Duration;
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
        final BrasilApiAddress cached = new BrasilApiAddress("89883000", "SC", "City", "Neighborhood", "Street", "svc");
        when(cache.get("89883000")).thenReturn(Optional.of(cached));

        final BrasilApiAddress result = service.findByCep("89883000");

        assertNotNull(result);
        assertEquals("89883000", result.getCep());
        verify(cache, times(1)).get("89883000");
        verifyNoInteractions(client);
    }

    @Test
    void findByCep_callsExternalAndUpdatesCache_whenNotInCache() {
        when(cache.get("89883000")).thenReturn(Optional.empty());

        ExternalBrasilApiDTO dto = new ExternalBrasilApiDTO();
        dto.setCep("89883000");
        dto.setState("SC");
        dto.setCity("City");
        dto.setNeighborhood("Neighborhood");
        dto.setStreet("Street");
        dto.setService("svc");

        when(client.fetchByCep("89883000")).thenReturn(dto);

        final BrasilApiAddress result = service.findByCep("89883000");

        assertNotNull(result);
        assertEquals("89883000", result.getCep());

        ArgumentCaptor<BrasilApiAddress> captor = ArgumentCaptor.forClass(BrasilApiAddress.class);
        verify(cache).put(eq("89883000"), captor.capture(), eq(Duration.ofSeconds(60)));

        BrasilApiAddress cached = captor.getValue();
        assertEquals("89883000", cached.getCep());
    }
}

