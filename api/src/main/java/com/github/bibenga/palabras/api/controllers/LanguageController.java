package com.github.bibenga.palabras.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.bibenga.palabras.entities.Language;
import com.github.bibenga.palabras.repositories.LanguageRepository;

import lombok.extern.log4j.Log4j2;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/language")
@Validated
@Log4j2
@Transactional(readOnly = true)
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleError(final Throwable e, Principal principal) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(detail);
    }

    @GetMapping(produces = { "application/json" })
    @ResponseBody
    public Page<LanguageDTO> getLanguages(Principal principal) {
        var dbLangs = languageRepository.findAll();
        var respLangs = new ArrayList<LanguageDTO>(dbLangs.size());
        for (var dbLang : dbLangs) {
            respLangs.add(convertLangToDto(dbLang));
        }
        log.info("find {} langs: {}", respLangs.size(), respLangs);
        return new PageImpl<>(respLangs);
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    @ResponseBody
    public LanguageDTO getOne(@PathVariable int id, Principal principal) throws LanguageNotFoundException {
        log.info("try find a language with id: {}", id);
        var dbLang = languageRepository.findById((byte) id);
        if (!dbLang.isPresent()) {
            log.info("a language with id {} is not found", id);
            throw new LanguageNotFoundException(id);
        }
        var respLang = convertLangToDto(dbLang.get());
        log.info("the language with id {} is: {}", id, respLang);
        return respLang;
    }

    private static final LanguageDTO convertLangToDto(Language lang) {
        return LanguageDTO.builder()
                .setId(lang.getId())
                .setCode(lang.getCode())
                .setName(lang.getName())
                .build();
    }
}
