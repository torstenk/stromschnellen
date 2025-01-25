package no.kirschner.stromschnellen;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "stromschnelle")
public record StromschnellenProperties (
        @DefaultValue("stromschnellenName") String name,
        @DefaultValue("flussName") String flussName) {

}