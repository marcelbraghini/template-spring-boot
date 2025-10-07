package template_spring_boot.template.brasilapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrasilApiCacheTest {
    @Mock
    private StringRedisTemplate redis;

    @Mock
    private ValueOperations<String, String> ops;

    @Mock
    private ObjectMapper mapper;

    private BrasilApiCache cache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redis.opsForValue()).thenReturn(ops);
        cache = new BrasilApiCache(redis, mapper);
    }

    @Test
    void get_returnsEmptyWhenNotPresent() {
        when(ops.get("cep:89883000")).thenReturn(null);

        Optional<BrasilApiAddress> result = cache.get("89883000");

        assertTrue(result.isEmpty());
        verify(ops, times(1)).get("cep:89883000");
    }

    @Test
    void get_returnsValueWhenPresent() throws Exception {
        final String json = "{\"cep\":\"89883000\",\"state\":\"SC\"}";
        final BrasilApiAddress address = new BrasilApiAddress("89883000", "SC", "City", "Neighborhood", "Street", "service");

        when(ops.get("cep:89883000")).thenReturn(json);
        when(mapper.readValue(json, BrasilApiAddress.class)).thenReturn(address);

        Optional<BrasilApiAddress> result = cache.get("89883000");

        assertTrue(result.isPresent());
        assertEquals("89883000", result.get().getCep());
        verify(ops).get("cep:89883000");
        verify(mapper).readValue(json, BrasilApiAddress.class);
    }

    @Test
    void put_withTtl_callsRedisSetWithTtl() throws JsonProcessingException {
        final BrasilApiAddress address = new BrasilApiAddress("89883000", "SC", "City", "Neighborhood", "Street", "service");
        final String json = "some-json";
        final Duration ttl = Duration.ofSeconds(30);

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put("89883000", address, ttl);

        verify(mapper).writeValueAsString(address);
        verify(ops).set("cep:89883000", json, ttl);
    }

    @Test
    void put_withoutTtl_callsRedisSetWithoutTtl() throws JsonProcessingException {
        final BrasilApiAddress address = new BrasilApiAddress("89883000", "SC", "City", "Neighborhood", "Street", "service");
        final String json = "some-json";

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put("89883000", address, null);

        verify(mapper).writeValueAsString(address);
        verify(ops).set("cep:89883000", json);
    }
}
