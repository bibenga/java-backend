package com.github.bibenga.palabras.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends Exception {
    public LanguageNotFoundException() {
        super();
    }

    public LanguageNotFoundException(int id) {
        super(String.format("language with id %s is not found", id));
    }
}