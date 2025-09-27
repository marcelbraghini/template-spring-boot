package template_spring_boot.template.brasilapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;

import java.time.Duration;
import java.util.Optional;

@Component
public class BrasilApiCache {
    private static final Logger logger = LoggerFactory.getLogger(BrasilApiCache.class);
    private static final String KEY_PREFIX = "cep:";

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public BrasilApiCache(final StringRedisTemplate redis, final ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    private String key(final String cep) {
        return KEY_PREFIX + cep;
    }

    public Optional<BrasilApiAddress> get(final String cep) {
        try {
            final String v = redis.opsForValue().get(key(cep));

            if (v == null) {
                return Optional.empty();
            }

            final BrasilApiAddress brasilApiAddress = mapper.readValue(v, BrasilApiAddress.class);
            return Optional.of(brasilApiAddress);
        } catch (final JsonProcessingException ex) {
            logger.error("Failed to deserialize cached value for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        } catch (final Exception ex) {
            logger.error("Redis error on get for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        }
    }

    public void put(final String cep, final BrasilApiAddress brasilApiAddress, final Duration ttl) {
        try {
            final String json = mapper.writeValueAsString(brasilApiAddress);

            if (ttl == null) {
                redis.opsForValue().set(key(cep), json);
            } else {
                redis.opsForValue().set(key(cep), json, ttl);
            }
        } catch (JsonProcessingException ex) {
            logger.error("Failed to serialize value for cep {}: {}", cep, ex.getMessage());
        } catch (Exception ex) {
            logger.error("Redis error on put for cep {}: {}", cep, ex.getMessage());
        }
    }
}

