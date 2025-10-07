package template_spring_boot.template.brasilapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import template_spring_boot.template.testutil.TestFixtures;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        when(redis.opsForValue()).thenReturn(ops);
        cache = new BrasilApiCache(redis, mapper);
    }

    @Test
    void get_returnsEmptyWhenNotPresent() {
        when(ops.get(TestFixtures.SAMPLE_CEP_KEY)).thenReturn(null);

        Optional<BrasilApiAddress> result = cache.get(TestFixtures.SAMPLE_CEP);

        assertTrue(result.isEmpty());
        verify(ops, times(1)).get(TestFixtures.SAMPLE_CEP_KEY);
    }

    @Test
    void get_returnsValueWhenPresent() throws Exception {
        final String json = TestFixtures.SAMPLE_JSON;
        final BrasilApiAddress address = TestFixtures.sampleAddress();

        when(ops.get(TestFixtures.SAMPLE_CEP_KEY)).thenReturn(json);
        when(mapper.readValue(json, BrasilApiAddress.class)).thenReturn(address);

        Optional<BrasilApiAddress> result = cache.get(TestFixtures.SAMPLE_CEP);

        assertTrue(result.isPresent());
        assertEquals(TestFixtures.SAMPLE_CEP, result.get().getCep());
        verify(ops).get(TestFixtures.SAMPLE_CEP_KEY);
        verify(mapper).readValue(json, BrasilApiAddress.class);
    }

    @Test
    void put_withTtl_callsRedisSetWithTtl() throws JsonProcessingException {
        final BrasilApiAddress address = TestFixtures.sampleAddress();
        final String json = "some-json";
        final Duration ttl = TestFixtures.SHORT_TTL;

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put(TestFixtures.SAMPLE_CEP, address, ttl);

        verify(mapper).writeValueAsString(address);
        verify(ops).set(TestFixtures.SAMPLE_CEP_KEY, json, ttl);
    }

    @Test
    void put_withoutTtl_callsRedisSetWithoutTtl() throws JsonProcessingException {
        final BrasilApiAddress address = TestFixtures.sampleAddress();
        final String json = "some-json";

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put(TestFixtures.SAMPLE_CEP, address, null);

        verify(mapper).writeValueAsString(address);
        verify(ops).set(TestFixtures.SAMPLE_CEP_KEY, json);
    }
}
