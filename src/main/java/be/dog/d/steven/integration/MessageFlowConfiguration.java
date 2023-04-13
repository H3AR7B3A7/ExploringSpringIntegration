package be.dog.d.steven.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;

@Configuration
public class MessageFlowConfiguration {

    @Bean
    MessageChannel messageChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    IntegrationFlow messageFlow() {
        return IntegrationFlow
                .from(
                        (MessageSource<String>) () -> MessageBuilder.withPayload(text()).build(),
                        poller -> poller.poller(pm -> pm.fixedRate(1000)))
//                .filter(String.class, source -> source.contains("hola"))
                .transform((GenericTransformer<String, String>) String::toUpperCase)
//               .channel(messageChannel())
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println("the payload is " + payload);
                    return null;
                })
                .get();
    }

    private static String text() {
        return Math.random() > .5 ?
                "Hello world | " + Instant.now() :
                "hola todo el munde | " + Instant.now();
    }

//    @Bean
//    IntegrationFlow messageFlow2() {
//        return IntegrationFlow
//                .from(messageChannel())
//                .handle((GenericHandler<String>) (payload, headers) -> {
//                    System.out.println("The payload is " + payload);
//                    return null;
//                })
//                .get();
//    }
}
