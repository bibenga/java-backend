package com.sl.palabras.services.impl;

import com.sl.palabras.entities.Language;
import com.sl.palabras.repositories.LanguageRepository;
import com.sl.palabras.services.LanguageService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("languageService")
@Transactional(readOnly = true)
@Log4j2
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    @Transactional(readOnly = false)
    public void addDefaultLanguages() {
        log.info("adds default languages if they is not already defined");
        var en = languageRepository.findOneByCode("en");
        if (en == null) {
            log.info("adds english languages");
            languageRepository.save(Language.builder()
                    .setId((byte) 1)
                    .setCode("en")
                    .setName("English")
                    .build());
        }

        var es = languageRepository.findOneByCode("es");
        if (es == null) {
            log.info("adds español languages");
            languageRepository.save(Language.builder()
                    .setId((byte) 2)
                    .setCode("es")
                    .setName("Español")
                    .build());
        }
    }
}
