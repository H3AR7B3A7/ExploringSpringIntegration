package be.dog.d.steven.integration.controller;

import be.dog.d.steven.integration.service.SomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SomeController {

    private final SomeService someService;

    @GetMapping("/hello")
    public String hello() {
        return someService.getGreeting();
    }
}
