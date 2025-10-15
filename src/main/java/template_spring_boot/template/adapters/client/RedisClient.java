package template_spring_boot.template.adapters.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import template_spring_boot.template.domain.Address;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisClient {
    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private static final String KEY_PREFIX = "cep:";

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public RedisClient(final StringRedisTemplate redis, final ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    private String key(final String cep) {
        return KEY_PREFIX + cep;
    }

    public Optional<Address> get(final String cep) {
        try {
            final String v = redis.opsForValue().get(key(cep));

            if (v == null) {
                return Optional.empty();
            }

            final Address address = mapper.readValue(v, Address.class);
            return Optional.of(address);
        } catch (final JsonProcessingException ex) {
            logger.error("Failed to deserialize cached value for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        } catch (final Exception ex) {
            logger.error("Redis error on get for cep {}: {}", cep, ex.getMessage());
            return Optional.empty();
        }
    }

    public void put(final String cep, final Address address, final Duration ttl) {
        try {
            final String json = mapper.writeValueAsString(address);

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

