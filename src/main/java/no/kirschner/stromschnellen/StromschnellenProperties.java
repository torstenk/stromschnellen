package no.kirschner.stromschnellen;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "stromschnelle")
public record StromschnellenProperties (
        @DefaultValue("defaultStromschnellenName") String name,
        @DefaultValue("defaultFlussName") String fluss) {
}