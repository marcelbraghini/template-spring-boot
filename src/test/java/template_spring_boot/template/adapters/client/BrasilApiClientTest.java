package template_spring_boot.template.adapters.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import template_spring_boot.template.adapters.client.dto.AddressResponse;
import template_spring_boot.template.application.exceptions.ExternalServiceException;
import template_spring_boot.template.application.exceptions.NotFoundCepException;
import template_spring_boot.template.fixture.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void fetchAddressByCep_success_returnsDto() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final AddressResponse dto = TestFixtures.sampleExternalDto();

        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;
        when(restTemplate.getForEntity(expectedUrl, AddressResponse.class))
                .thenReturn(ResponseEntity.ok(dto));

        final AddressResponse result = client.fetchAddressByCep(cep);

        assertNotNull(result);
        assertEquals(cep, result.getCep());
        verify(restTemplate).getForEntity(expectedUrl, AddressResponse.class);
    }

    @Test
    void fetchAddressByCep_404_throwsExternalServiceException() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;

        when(restTemplate.getForEntity(expectedUrl, AddressResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ExternalServiceException.class, () -> client.fetchAddressByCep(cep));
        verify(restTemplate).getForEntity(expectedUrl, AddressResponse.class);
    }

    @Test
    void fetchAddressByCep_okButNoBody_throwsNotFoundCepException() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final String expectedUrl = "https://brasilapi.com.br/api/cep/v2/" + cep;

        when(restTemplate.getForEntity(expectedUrl, AddressResponse.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(NotFoundCepException.class, () -> client.fetchAddressByCep(cep));
        verify(restTemplate).getForEntity(expectedUrl, AddressResponse.class);
    }
}
