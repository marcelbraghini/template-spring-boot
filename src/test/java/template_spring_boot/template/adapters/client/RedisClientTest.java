package template_spring_boot.template.adapters.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import template_spring_boot.template.domain.Address;
import template_spring_boot.template.fixture.TestFixtures;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisClientTest {
    @Mock
    private StringRedisTemplate redis;

    @Mock
    private ValueOperations<String, String> ops;

    @Mock
    private ObjectMapper mapper;

    private RedisClient cache;

    @BeforeEach
    void setUp() {
        when(redis.opsForValue()).thenReturn(ops);
        cache = new RedisClient(redis, mapper);
    }

    @Test
    void get_returnsEmptyWhenNotPresent() {
        when(ops.get(TestFixtures.SAMPLE_CEP_KEY)).thenReturn(null);

        Optional<Address> result = cache.get(TestFixtures.SAMPLE_CEP);

        assertTrue(result.isEmpty());
        verify(ops, times(1)).get(TestFixtures.SAMPLE_CEP_KEY);
    }

    @Test
    void get_returnsValueWhenPresent() throws Exception {
        final String json = TestFixtures.SAMPLE_JSON;
        final Address address = TestFixtures.sampleAddress();

        when(ops.get(TestFixtures.SAMPLE_CEP_KEY)).thenReturn(json);
        when(mapper.readValue(json, Address.class)).thenReturn(address);

        Optional<Address> result = cache.get(TestFixtures.SAMPLE_CEP);

        assertTrue(result.isPresent());
        assertEquals(TestFixtures.SAMPLE_CEP, result.get().getCep());
        verify(ops).get(TestFixtures.SAMPLE_CEP_KEY);
        verify(mapper).readValue(json, Address.class);
    }

    @Test
    void put_withTtl_callsRedisSetWithTtl() throws JsonProcessingException {
        final Address address = TestFixtures.sampleAddress();
        final String json = "some-json";
        final Duration ttl = TestFixtures.SHORT_TTL;

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put(TestFixtures.SAMPLE_CEP, address, ttl);

        verify(mapper).writeValueAsString(address);
        verify(ops).set(TestFixtures.SAMPLE_CEP_KEY, json, ttl);
    }

    @Test
    void put_withoutTtl_callsRedisSetWithoutTtl() throws JsonProcessingException {
        final Address address = TestFixtures.sampleAddress();
        final String json = "some-json";

        when(mapper.writeValueAsString(address)).thenReturn(json);

        cache.put(TestFixtures.SAMPLE_CEP, address, null);

        verify(mapper).writeValueAsString(address);
        verify(ops).set(TestFixtures.SAMPLE_CEP_KEY, json);
    }
}
