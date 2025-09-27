package template_spring_boot.template.brasilapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template_spring_boot.template.brasilapi.dto.BrasilApiAddressResponse;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.service.BrasilApiService;

@RestController
@RequestMapping("/brasilapi")
public class BrasilApiController {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiController.class);

    private final BrasilApiService brasilApiService;

    public BrasilApiController(final BrasilApiService brasilApiService) {
        this.brasilApiService = brasilApiService;
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<BrasilApiAddressResponse> getByCep(@PathVariable("cep") final String cep) {
        final BrasilApiAddress brasilApiAddress = brasilApiService.findByCep(cep);
        final BrasilApiAddressResponse brasilApiAddressResponse = BrasilApiAddressResponse.fromEntity(brasilApiAddress);
        logger.info("Received request for CEP {}", cep);
        return ResponseEntity.ok(brasilApiAddressResponse);
    }
}

