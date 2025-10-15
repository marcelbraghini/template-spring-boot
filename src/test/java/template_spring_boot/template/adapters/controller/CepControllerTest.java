package template_spring_boot.template.adapters.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import template_spring_boot.template.adapters.controller.dto.AddressResponse;
import template_spring_boot.template.application.service.BrasilApiService;
import template_spring_boot.template.domain.Address;
import template_spring_boot.template.fixture.TestFixtures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepControllerTest {

    @Mock
    private BrasilApiService brasilApiService;

    @InjectMocks
    private CepController cepController;

    @Test
    void testGetAddressByCep_ReturnsAddressResponse() {
        final String cep = TestFixtures.SAMPLE_CEP;
        final Address mockAddress = TestFixtures.sampleAddress();
        when(brasilApiService.findAddressByCep(cep)).thenReturn(mockAddress);

        AddressResponse expectedResponse = AddressResponse.fromEntity(mockAddress);
        ResponseEntity<AddressResponse> response = cepController.getAddressByCep(cep);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(brasilApiService, times(1)).findAddressByCep(captor.capture());
        assertEquals(cep, captor.getValue());

        verifyNoMoreInteractions(brasilApiService);
    }
}
