package no.kirschner.stromschnellen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(StromschnellenProperties.class)
public class StromschnellenAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(StromschnellenAutoConfiguration.class);

  @Autowired
  private StromschnellenProperties stromschnellenProperties;

  @Autowired
  private Environment env;

  @Bean
  @ConditionalOnMissingBean
  public Map<String, String> streamProperties() {
    Map<String, String> properties = new HashMap<>();

    String name = stromschnellenProperties.name().toLowerCase();
    String fluss = stromschnellenProperties.fluss().toLowerCase();

    // Set the destination and group for the input binding
    properties.put("spring.cloud.stream.bindings." + fluss + "-in-0.destination", env.getProperty("KAFKA_RAPID_TOPIC", name));
    properties.put("spring.cloud.stream.bindings." + fluss + "-in-0.group", fluss + "-group");

    // Set the destination for the output binding
    properties.put("spring.cloud.stream.bindings." + fluss + "-out-0.destination", env.getProperty("KAFKA_RAPID_TOPIC", name));

    // Set the Kafka brokers
    properties.put("spring.cloud.stream.kafka.binder.brokers", env.getProperty("KAFKA_BROKERS", "localhost:9092"));

    // Set the function definition
    properties.put("spring.cloud.function.definition", fluss);

    log.info("Configured properties: {}", properties);
    return properties;
  }
}