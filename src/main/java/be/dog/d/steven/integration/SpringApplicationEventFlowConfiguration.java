package be.dog.d.steven.integration;

import be.dog.d.steven.integration.event.MyCustomEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class SpringApplicationEventFlowConfiguration {

    @Bean
    MessageChannel eventChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public ApplicationEventListeningMessageProducer eventsAdapter() {
        ApplicationEventListeningMessageProducer producer =
                new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(ApplicationStartedEvent.class);
        return producer;
    }

    @Bean
    public IntegrationFlow eventFlow(ApplicationEventListeningMessageProducer eventsAdapter, MessageChannel eventChannel) {
        return IntegrationFlow.from(eventsAdapter)
//                .handle((GenericHandler<ApplicationStartedEvent>) (payload, headers) -> {
//                    System.out.println("!!! Application started: " + payload + "::" + headers + "!!!");
//                    return null;
//                })
                .channel(eventChannel)
        .get();
    }

    @Bean
    IntegrationFlow eventFlow2() {
        return IntegrationFlow
                .from(eventChannel())
                .handle((GenericHandler<ApplicationStartedEvent>) (payload, headers) -> {
                    System.out.println("!!! Application started: " + payload + "::" + headers + "!!!");
                    return null;
                })
                .get();
    }

    @Bean
    public ApplicationEventListeningMessageProducer myCustomEventsAdapter() {
        ApplicationEventListeningMessageProducer producer =
                new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(MyCustomEvent.class);
        return producer;
    }

    @Bean
    public IntegrationFlow myCustomEventFlow(ApplicationEventListeningMessageProducer myCustomEventsAdapter) {
        return IntegrationFlow.from(myCustomEventsAdapter)
                .handle((GenericHandler<MyCustomEvent>) (payload, headers) -> {
                    System.out.println("!!! My custom event: " + payload.getMessage() + "::" + headers + "!!!");
                    return null;
                })
                .get();
    }
}
