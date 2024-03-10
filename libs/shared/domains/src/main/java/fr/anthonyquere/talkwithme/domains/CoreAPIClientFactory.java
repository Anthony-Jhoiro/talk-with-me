package fr.anthonyquere.talkwithme.domains;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class CoreAPIClientFactory {
  private final String apiEndpoint = "http://localhost:8080";
  private final String companionId = "vulpis";
  private final CoreAPI coreAPI;

  public CoreAPIClientFactory() {

    ObjectMapper om = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    coreAPI = Feign.builder()
      .encoder(new JacksonEncoder(om))
      .decoder(new JacksonDecoder(om))
      .target(CoreAPI.class, apiEndpoint);
  }

  public CoreAPI getClient() {
    return coreAPI;
  }


}
