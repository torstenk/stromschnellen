package no.kirschner.stromschnellen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(StromschnellenProperties.class)
public class StromschnellenConfiguration {
  public static final Logger log = LoggerFactory.getLogger(StromschnellenConfiguration.class);
  private final StromschnellenProperties properties;

  public StromschnellenConfiguration(StromschnellenProperties properties) {
    this.properties = properties;
    log.info("StromschnellenConfiguration created with properties {}", properties);
  }

  @Bean
  public PropertySourcesPlaceholderConfigurer propertyConfigurer(ConfigurableEnvironment environment) {
    // Get dynamic values
    String stromschnenlleName = properties.name();
    String flussName = properties.flussName();

    // Create dynamic spring.cloud.stream properties
    Map<String, Object> dynamicProperties = new HashMap<>();
    dynamicProperties.put("spring.cloud.stream.bindings." + flussName + "-in-0.destination", stromschnenlleName);
    dynamicProperties.put("spring.cloud.stream.bindings." + flussName + "-in-0.group", stromschnenlleName + "Gruppe");
    dynamicProperties.put("spring.cloud.stream.bindings." + flussName + "-out-0.destination", stromschnenlleName);
    dynamicProperties.put("spring.cloud.stream.kafka.binder.brokers", "${KAFKA_BROKERS:localhost:9092}");
    dynamicProperties.put("spring.cloud.stream.function.definition", flussName);

    // Add properties to environment
    environment.getPropertySources().addFirst(new MapPropertySource("dynamicStromschnellenConfig", dynamicProperties));

    return new PropertySourcesPlaceholderConfigurer();
  }
}