package be.dog.d.steven.integration.service;

import be.dog.d.steven.integration.event.MyCustomEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SomeService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public String getGreeting() {
        applicationEventPublisher.publishEvent(new MyCustomEvent(this, "Blaat"));
        return "Hello World!";
    }
}
