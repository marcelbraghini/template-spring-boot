package template_spring_boot.template.brasilapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import template_spring_boot.template.brasilapi.dto.BrasilApiAddressResponse;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.service.BrasilApiService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrasilApiControllerTest {

    @Mock
    private BrasilApiService brasilApiService;

    @InjectMocks
    private BrasilApiController brasilApiController;

    @Test
    void testGetByCep_ReturnsAddressResponse() {
        final String cep = "89883000";
        final BrasilApiAddress mockAddress = new BrasilApiAddress();
        when(brasilApiService.findByCep(cep)).thenReturn(mockAddress);

        BrasilApiAddressResponse expectedResponse = BrasilApiAddressResponse.fromEntity(mockAddress);
        ResponseEntity<BrasilApiAddressResponse> response = brasilApiController.getByCep(cep);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(brasilApiService, times(1)).findByCep(captor.capture());
        assertEquals(cep, captor.getValue());

        verifyNoMoreInteractions(brasilApiService);
    }
}
