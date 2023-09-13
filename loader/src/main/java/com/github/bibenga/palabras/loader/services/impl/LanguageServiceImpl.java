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
        var en = languageRepository.findOneByCode("en");
        if (en == null) {
            log.info("the english language is added");
            languageRepository.save(Language.builder()
                    .setId((byte) 1)
                    .setCode("en")
                    .setName("English")
                    .build());
        } else {
            log.info("the english language have been already added");
        }

        var es = languageRepository.findOneByCode("es");
        if (es == null) {
            log.info("the español language is added");
            languageRepository.save(Language.builder()
                    .setId((byte) 2)
                    .setCode("es")
                    .setName("Español")
                    .build());
        } else {
            log.info("the español language have been already added");
        }
    }
}
