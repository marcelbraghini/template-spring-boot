package template_spring_boot.template.brasilapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import template_spring_boot.template.brasilapi.cache.BrasilApiCache;
import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.brasilapi.external.BrasilApiClient;
import template_spring_boot.template.brasilapi.validation.CepValidator;

import java.time.Duration;
import java.util.Optional;

@Service
public class BrasilApiService {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiService.class);
    private static final Duration CACHE_TTL = Duration.ofSeconds(60);

    private final BrasilApiClient brasilApiClient;
    private final BrasilApiCache cache;

    public BrasilApiService(BrasilApiClient brasilApiClient, BrasilApiCache cache) {
        this.brasilApiClient = brasilApiClient;
        this.cache = cache;
    }

    public BrasilApiAddress findByCep(final String cep) {
        final String normalized = CepValidator.normalize(cep);
        CepValidator.validate(cep, normalized);

        logger.info("Searching for cep {} (normalized {})", cep, normalized);

        Optional<BrasilApiAddress> cached = getFromCache(normalized);

        if (cached.isPresent()) {
            logger.info("Cache hit for cep {}", normalized);
            return cached.get();
        }

        ExternalBrasilApiDTO externalBrasilApiDTO = brasilApiClient.fetchByCep(normalized);
        BrasilApiAddress brasilApiAddress = new BrasilApiAddress(
                externalBrasilApiDTO.getCep(),
                externalBrasilApiDTO.getState(),
                externalBrasilApiDTO.getCity(),
                externalBrasilApiDTO.getNeighborhood(),
                externalBrasilApiDTO.getStreet(),
                externalBrasilApiDTO.getService()
        );

        updateCache(normalized, brasilApiAddress);

        return brasilApiAddress;
    }

    private Optional<BrasilApiAddress> getFromCache(String cep) {
        try {
            return cache.get(cep);
        } catch (Exception ex) {
            logger.error("Cache check failed for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        }
    }

    private void updateCache(final String cep, final BrasilApiAddress brasilApiAddress) {
        try {
            cache.put(cep, brasilApiAddress, CACHE_TTL);
            logger.info("Cached cep {} for {} seconds", cep, CACHE_TTL.getSeconds());
        } catch (Exception ex) {
            logger.error("Failed to update cache for cep {}: {}", cep, ex.getMessage());
        }
    }
}
