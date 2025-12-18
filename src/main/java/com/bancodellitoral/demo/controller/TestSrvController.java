package com.bancodellitoral.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/javax")
@Slf4j
public class TestSrvController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test() throws InterruptedException {
        MDC.put("traceId", UUID.randomUUID().toString());
        MDC.put("spanId", UUID.randomUUID().toString());
        log.info("init {}", UUID.randomUUID());
//        Thread.sleep(1000);
//        log.info("end");
        return "javax";
    }

}
