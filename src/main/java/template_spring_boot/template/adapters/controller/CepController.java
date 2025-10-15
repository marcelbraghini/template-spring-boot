package template_spring_boot.template.adapters.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template_spring_boot.template.adapters.controller.dto.AddressResponse;
import template_spring_boot.template.application.service.BrasilApiService;
import template_spring_boot.template.domain.Address;

@RestController
@RequestMapping("/v1")
public class CepController {
    private static final Logger logger = LoggerFactory.getLogger(CepController.class);

    @Autowired
    private BrasilApiService brasilApiService;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<AddressResponse> getAddressByCep(@PathVariable("cep") final String cep) {
        logger.info("Received request for CEP: {}", cep);
        final Address address = brasilApiService.findAddressByCep(cep);
        final AddressResponse addressResponse = AddressResponse.fromEntity(address);
        return ResponseEntity.ok(addressResponse);
    }
}
