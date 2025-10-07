package template_spring_boot.template.brasilapi.external;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.exceptions.ExternalServiceException;
import template_spring_boot.template.brasilapi.exceptions.NotFoundCepException;
import template_spring_boot.template.testutil.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrasilApiClientTest {
    @Mock
    private RestTemplate restTemplate;

    private BrasilApiClient client;

    @BeforeEach
    void setUp() {
        client = new BrasilApiClient(restTemplate, "https://brasilapi.com.br/");
    }

    @Test
    void fetchByCep_success_returnsDto() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final ExternalBrasilApiDTO dto = TestFixtures.sampleExternalDto();

        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;
        when(restTemplate.getForEntity(expectedUrl, ExternalBrasilApiDTO.class))
                .thenReturn(ResponseEntity.ok(dto));

        final ExternalBrasilApiDTO result = client.fetchByCep(cep);

        assertNotNull(result);
        assertEquals(cep, result.getCep());
        verify(restTemplate).getForEntity(expectedUrl, ExternalBrasilApiDTO.class);
    }

    @Test
    void fetchByCep_404_throwsExternalServiceException() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;

        when(restTemplate.getForEntity(expectedUrl, ExternalBrasilApiDTO.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ExternalServiceException.class, () -> client.fetchByCep(cep));
        verify(restTemplate).getForEntity(expectedUrl, ExternalBrasilApiDTO.class);
    }

    @Test
    void fetchByCep_okButNoBody_throwsNotFoundCepException() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;

        when(restTemplate.getForEntity(expectedUrl, ExternalBrasilApiDTO.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(NotFoundCepException.class, () -> client.fetchByCep(cep));
        verify(restTemplate).getForEntity(expectedUrl, ExternalBrasilApiDTO.class);
    }
}
