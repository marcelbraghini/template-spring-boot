package template_spring_boot.template.brasilapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template_spring_boot.template.brasilapi.cache.BrasilApiCache;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.external.BrasilApiClient;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.exceptions.ExternalServiceException;
import template_spring_boot.template.brasilapi.exceptions.InvalidCepException;
import template_spring_boot.template.brasilapi.exceptions.NotFoundCepException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

public class BrasilApiServiceTest {

    private BrasilApiClient client;
    private BrasilApiCache cache;
    private BrasilApiService service;

    @BeforeEach
    public void setUp() {
        client = Mockito.mock(BrasilApiClient.class);
        cache = Mockito.mock(BrasilApiCache.class);
        // default cache behavior: miss
        when(cache.get(anyString())).thenReturn(Optional.empty());
        service = new BrasilApiService(client, cache);
    }

    @Test
    public void testFindByCep_success() {
        ExternalBrasilApiDTO dto = new ExternalBrasilApiDTO();
        dto.setCep("89883000");
        dto.setState("SC");
        dto.setCity("Águas de Chapecó");
        dto.setNeighborhood(null);
        dto.setStreet(null);
        dto.setService("open-cep");

        when(client.fetchByCep("89883000")).thenReturn(dto);

        BrasilApiAddress result = service.findByCep("89883000");
        assertNotNull(result);
        assertEquals("89883000", result.getCep());
        assertEquals("SC", result.getState());
        assertEquals("Águas de Chapecó", result.getCity());
        assertNull(result.getNeighborhood());
        assertNull(result.getStreet());
        assertEquals("open-cep", result.getService());
    }

    @Test
    public void testFindByCep_invalidCep() {
        assertThrows(InvalidCepException.class, () -> service.findByCep("abc"));
        assertThrows(InvalidCepException.class, () -> service.findByCep(null));
    }

    @Test
    public void testFindByCep_notFound() {
        when(client.fetchByCep("00000000")).thenThrow(new NotFoundCepException("CEP not found"));
        assertThrows(NotFoundCepException.class, () -> service.findByCep("00000000"));
    }

    @Test
    public void testFindByCep_externalError() {
        when(client.fetchByCep("11111111")).thenThrow(new ExternalServiceException("external error"));
        assertThrows(ExternalServiceException.class, () -> service.findByCep("11111111"));
    }
}
