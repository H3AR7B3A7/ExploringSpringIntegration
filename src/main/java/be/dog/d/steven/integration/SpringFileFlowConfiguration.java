package be.dog.d.steven.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.util.SystemPropertyUtils;

import java.io.File;

@Configuration
public class SpringFileFlowConfiguration {

    @Bean
    DirectChannel fileRequests() {
        return MessageChannels.direct().get();
    }

    @Bean
    DirectChannel fileReplies() {
        return MessageChannels.direct().get();
    }

    @Bean
    IntegrationFlow inboundFileSystemFlow() {
        File dir = new File(SystemPropertyUtils.resolvePlaceholders("${HOME}/Desktop/in"));
        return IntegrationFlow
                .from(Files.inboundAdapter(dir)
                        .autoCreateDirectory(true))
                .transform(new FileToStringTransformer())
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println(">>> IN:");
                    headers.forEach((key, value) -> System.out.println(key + "=" + value));
                    return payload;
                })
                .channel(fileRequests())
                .get();
    }

    @Bean
    IntegrationFlow fileFlow() {
        return IntegrationFlow
                .from(fileRequests())
                .transform((GenericTransformer<String, String>) String::toUpperCase)
                .channel(fileReplies())
                .get();
    }

    @Bean
    IntegrationFlow outboundFileSystemFlow() {
        File dir = new File(SystemPropertyUtils.resolvePlaceholders("${HOME}/Desktop/out"));
        return IntegrationFlow
                .from(fileReplies())
                .enrichHeaders(
                        spec -> spec.headerExpression(
                                FileHeaders.FILENAME,
                                String.format("'new_' + headers[%s].name", FileHeaders.ORIGINAL_FILE),
                                true))
                .handle(Files.outboundAdapter(dir)
                        .autoCreateDirectory(true))
                .get();
    }
}
