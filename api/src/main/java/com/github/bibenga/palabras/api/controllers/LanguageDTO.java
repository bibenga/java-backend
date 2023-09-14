package com.github.bibenga.palabras.api.controllers;

import java.io.Serializable;

import com.github.bibenga.palabras.entities.Language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
public class LanguageDTO implements Serializable {
    private int id;

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    @NotBlank
    private String name;
    
    public LanguageDTO(Language lang) {
        id = lang.getId();
        code = lang.getCode();
        name = lang.getName();
    }
}