package template_spring_boot.template.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import template_spring_boot.template.adapters.client.BrasilApiClient;
import template_spring_boot.template.adapters.client.RedisClient;
import template_spring_boot.template.adapters.client.dto.AddressResponse;
import template_spring_boot.template.application.validation.CepValidator;
import template_spring_boot.template.domain.Address;

import java.time.Duration;
import java.util.Optional;

@Service
public class BrasilApiService {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiService.class);
    private static final Duration CACHE_TTL = Duration.ofSeconds(60);
    private final BrasilApiClient brasilApiClient;
    private final RedisClient redisClient;

    public BrasilApiService(final BrasilApiClient brasilApiClient, final RedisClient redisClient) {
        this.brasilApiClient = brasilApiClient;
        this.redisClient = redisClient;
    }

    public Address findAddressByCep(final String cep) {
        final String normalized = CepValidator.normalize(cep);
        CepValidator.validate(cep, normalized);

        logger.info("Searching for cep {} (normalized {})", cep, normalized);

        Optional<Address> addressFromCache = getAddressFromCache(normalized);

        if (addressFromCache.isPresent()) {
            logger.info("Cache hit for cep {}", normalized);
            return addressFromCache.get();
        }

        final AddressResponse addressResponse = brasilApiClient.fetchAddressByCep(normalized);
        final Address address = new Address(
                addressResponse.getCep(),
                addressResponse.getState(),
                addressResponse.getCity(),
                addressResponse.getNeighborhood(),
                addressResponse.getStreet(),
                addressResponse.getService()
        );

        updateCache(normalized, address);

        return address;
    }

    private Optional<Address> getAddressFromCache(final String cep) {
        try {
            return redisClient.get(cep);
        } catch (Exception ex) {
            logger.error("Cache check failed for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        }
    }

    private void updateCache(final String cep, final Address address) {
        try {
            redisClient.put(cep, address, CACHE_TTL);
            logger.info("Cached cep {} for {} seconds", cep, CACHE_TTL.getSeconds());
        } catch (Exception ex) {
            logger.error("Failed to update cache for cep {}: {}", cep, ex.getMessage());
        }
    }
}
