package com.github.bibenga.palabras.loader.services.impl;

import com.github.bibenga.palabras.entities.Language;
import com.github.bibenga.palabras.repositories.LanguageRepository;
import com.github.bibenga.palabras.loader.services.LanguageService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("languageService")
@Transactional
@Log4j2
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public void addDefaultLanguages() {
        log.info("adds default languages if they is not already defined");

        insertIfNotExists(Language.builder()
                .setId((byte) 1)
                .setCode("en")
                .setName("English")
                .build());

        insertIfNotExists(Language.builder()
                .setId((byte) 2)
                .setCode("es")
                .setName("Español")
                .build());
    }

    public void insertIfNotExists(Language lang) {
        var exists = languageRepository.existsByCode(lang.getCode());
        log.info("does the '{}' language exist? -> {}", lang.getName(), exists);
        if (!exists) {
            log.info("the '{}' language is added", lang.getName());
            languageRepository.save(lang);
        } else {
            log.info("the '{}' language have been already added", lang.getName());
        }
    }
}
