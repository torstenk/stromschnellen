package no.kirschner.stromschnellen;

import io.cloudevents.CloudEvent;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A River is a selective consumer of events from a PubSub channel, the Rapids, that it also publishes new events to.
 *<p>
 * Using <a href="https://spring.io/projects/spring-cloud-stream">Spring Cloud Stream</a>, this is implemented as a default method in this interface and applied like this:
 *<p>
 * {@snippet lang="java":
 *@Component
 * public class EventRiver implements River {
 *   @Bean
 *   public Function<CloudEvent, CloudEvent> anEventRiver() {
 *     return aRiver(
 *       ce -> "event".equals(ce.getType()),
 *       ce -> CloudEventBuilder.v1(ce)
 *        .withId(UUID.randomUUID().toString()).withType("event.processed")
 *        .build()
 *     );
 *   }
 * }
 *}
 *<p>
 * Configuration of this River in a Spring Boot 3 application:
 *<p>
 *{@snippet lang="properties":
 * spring.function.definition=anEventRiver
 * spring.cloud.stream.bindings.anEventRiver-in-0.destination=rapids
 * spring.cloud.stream.bindings.anEventRiver-out-0.destination=rapids
 * spring.cloud.stream.bindings.anEventRiver-in-0.group=anEventRiver-group
 * spring.cloud.stream.kafka.binder.brokers=${KAFKA_BROKERS:localhost:9092}
 *}
 *
 *<p>
 * Be careful to observe Spring Cloud Stream naming conventions.
 *<p>
 * The Rapids are called rapids in all destinations of the River, and for all Rivers in the application.
 * and the function definition is aDocumentRiver,
 * the name of the corresponding Spring bean.
 * The in and out parameters are in-0 and out-0 postfixes of the function name.
 */
public interface Fluss {

  /**
   * If the filter passes, the CloudEvent is processed and a new one returned;
   * otherwise, it is skipped.
   *
   * @param filter a Predicate to test whether the event should be processed
   * @param process a Function to process the event if the filter passes
   * @return a Function that takes a CloudEvent and returns a processed CloudEvent or null if skipped
   */
  default <T extends CloudEvent> Function<T, T> einFluss(Predicate<T> filter, Function<T, ? extends T> process) {
    return ce -> filter.test(ce) ? process.apply(ce) : null;
  }
}
