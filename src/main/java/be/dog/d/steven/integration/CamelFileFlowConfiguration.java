package be.dog.d.steven.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CamelFileFlowConfiguration {

    private final FileNameProcessor fileNameProcessor;
    private final FileContentProcessor fileContentProcessor;

    public CamelFileFlowConfiguration(FileNameProcessor fileNameProcessor, FileContentProcessor fileContentProcessor) {
        this.fileNameProcessor = fileNameProcessor;
        this.fileContentProcessor = fileContentProcessor;
    }

    @Bean
    RoutesBuilder routes() {
        return new RouteBuilder() {
            @Override
            public void configure() {
//                from("file://{{HOME}}/Desktop/in2?noop=true")
//                        .routeId("camel-in-to-out")
//                        .process(exchange -> exchange
//                                .getIn()
//                                .setHeader("CamelFileName", "new_" + exchange
//                                        .getIn()
//                                        .getHeader("CamelFileName")))
//                        .to("file://{{HOME}}/Desktop/out2");

                from("file://{{HOME}}/Desktop/in2?noop=true")
                        .routeId("camel-in")
                        .to("direct:process");

//                from("direct:process")
//                        .routeId("camel-process")
//                        .process(exchange -> exchange
//                                .getIn()
//                                .setHeader("CamelFileName", "new_" + exchange
//                                        .getIn()
//                                        .getHeader("CamelFileName")))
//                        .to("direct:out");

                from("direct:process")
                        .routeId("camel-process")
                        .process(fileNameProcessor)
                        .process(fileContentProcessor)
                        .to("direct:out");

                from("direct:out")
                        .routeId("camel-out")
                        .to("file://{{HOME}}/Desktop/out2");
            }
        };
    }

    @Component
    static class FileNameProcessor implements Processor {
        @Override
        public void process(Exchange exchange) {
            exchange.getIn()
                    .setHeader("CamelFileName", "new_" + exchange
                            .getIn()
                            .getHeader("CamelFileName"));
        }
    }

    @Component
    static class FileContentProcessor implements Processor {
        @Override
        public void process(Exchange exchange) {
            exchange.getIn()
                    .setBody(exchange
                            .getIn()
                            .getBody(String.class)
                            .toUpperCase());
        }
    }
}
