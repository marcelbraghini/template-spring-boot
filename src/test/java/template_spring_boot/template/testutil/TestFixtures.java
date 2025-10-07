package template_spring_boot.template.testutil;

import template_spring_boot.template.brasilapi.dto.ExternalBrasilApiDTO;
import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;
import java.time.Duration;

public final class TestFixtures {

    public static final String SAMPLE_CEP = "89883000";
    public static final String SAMPLE_CEP_KEY = "cep:89883000";
    public static final Duration DEFAULT_TTL = Duration.ofSeconds(60);
    public static final Duration SHORT_TTL = Duration.ofSeconds(30);
    public static final String SAMPLE_JSON = "{\"cep\":\"89883000\",\"state\":\"SC\"}";

    private TestFixtures() {}

    public static BrasilApiAddress sampleAddress() {
        return new BrasilApiAddress(SAMPLE_CEP, "SC", "City", "Neighborhood", "Street", "service");
    }

    public static ExternalBrasilApiDTO sampleExternalDto() {
        ExternalBrasilApiDTO dto = new ExternalBrasilApiDTO();
        dto.setCep(SAMPLE_CEP);
        dto.setState("SC");
        dto.setCity("City");
        dto.setNeighborhood("Neighborhood");
        dto.setStreet("Street");
        dto.setService("service");
        return dto;
    }
}
