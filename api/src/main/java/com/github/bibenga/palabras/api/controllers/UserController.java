package com.github.bibenga.palabras.api.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirements(value = @SecurityRequirement(name = "firestore"))
@Validated
@Log4j2
@Transactional(readOnly = true)
public class UserController {
    @GetMapping
    @ResponseBody
    public UserDTO getUser(Principal principal) throws LanguageNotFoundException {
        var userDto = UserDTO.builder()
                .uid(principal == null ? null : principal.getName())
                .authenticated(principal != null)
                .build();
        log.info("the user is: {}", userDto);
        return userDto;
    }

    // @GetMapping
    // @ResponseBody
    // @PreAuthorize("isAuthenticated()")
    // // @PreAuthorize("hasAuthority('USER')")
    // public UserDTO getCurrentUser(@NotNull Principal principal) throws
    // LanguageNotFoundException {
    // var userDto = UserDTO.builder()
    // .uid(principal.getName())
    // .authenticated(true)
    // .build();
    // log.info("the user is: {}", userDto);
    // return userDto;
    // }

    @PatchMapping
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    // @PreAuthorize("hasAuthority('USER')")
    public UserDTO patchUser(@RequestBody @Valid UserDTO request, @NotNull Principal principal)
            throws LanguageNotFoundException {
        var userDto = UserDTO.builder()
                .uid(principal.getName())
                .authenticated(true)
                .foto(request.getFoto())
                .build();
        log.info("the user is: {}", userDto);
        return userDto;
    }
}
