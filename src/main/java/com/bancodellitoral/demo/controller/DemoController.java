package com.bancodellitoral.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/test")
@Slf4j
public class DemoController {

    @GetMapping
    public String index() throws InterruptedException {
        MDC.put("traceId", UUID.randomUUID().toString());
        MDC.put("spanId", UUID.randomUUID().toString());
        log.info("init");
        Thread.sleep(1000);
        log.info("end");
        return "Hello World";
    }
}
