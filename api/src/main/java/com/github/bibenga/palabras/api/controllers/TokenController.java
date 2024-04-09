package com.github.bibenga.palabras.api.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/token", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Log4j2
public class TokenController {
    // Very secured code :)

    @Value("${palabras.firebase.token-uri}")
    private String tokenUriTemplate;

    @Value("${palabras.firebase.api-key}")
    private String apiKey;

    @GetMapping
    @ResponseBody
    public Map<String, Object> getOne(
            @RequestParam(name = "email", defaultValue = "olala-AE92ED63-7F80-4FD5-B875-5D72A9774510@gmail.com") @NotNull @NotBlank @Email String email,
            @RequestParam(name = "password", defaultValue = "9B725739-9470-4237-8A75-0B2391BAA4C6") @NotNull @NotBlank String password) {
        log.info("try obtain token for {}", email);

        var uri = UriComponentsBuilder.fromUriString(tokenUriTemplate).build();
        uri = uri.expand(Map.of("key", this.apiKey));
        var url = uri.toString();
        var params = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true);
        var api = new RestTemplate();
        Map<String, Object> res = api.postForObject(url, params, Map.class);
        return res;
    }
}
