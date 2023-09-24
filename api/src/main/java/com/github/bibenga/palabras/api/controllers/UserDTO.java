package com.github.bibenga.palabras.api.controllers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class UserDTO implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean authenticated;

    @NotBlank
    private String foto;
}