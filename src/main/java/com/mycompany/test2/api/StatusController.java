package com.mycompany.test2.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path = "/api/v1/management")
@Slf4j
public class StatusController {

    private static final ZonedDateTime started = ZonedDateTime.now(ZoneId.of("UTC"));
    private static final AtomicLong counter = new AtomicLong();

//     @SecurityRequirements(value = {
//         // @SecurityRequirement(name = "token"),
//         @SecurityRequirement(name = "basic")
// })
    @GetMapping(value = "/status", produces = { "application/json" })
    // @CrossOrigin("https://safelagooon.com")
    @ResponseBody
    public Map<String, Object> getStatus(Principal principal) {
        var now = ZonedDateTime.now(ZoneId.of("UTC"));
        var status = new LinkedHashMap<String, Object>();
        status.put("status", "OK");
        status.put("counter", counter.incrementAndGet());
        status.put("startedAt", started);
        status.put("serverTime", now);
        status.put("uptime", Duration.between(started, now));
        status.put("principal", (principal == null) ? "Anonymous" : principal.getName());
        log.info("status: {}", status);
        return status;
    }

    @PostMapping(value = "/status", produces = { "application/json" })
    @ResponseBody
    public Map<String, Object> postStatus(Principal principal) {
        var status = new LinkedHashMap<String, Object>();
        status.put("status", "OK");
        log.info("status: {}", status);
        return status;
    }
}
