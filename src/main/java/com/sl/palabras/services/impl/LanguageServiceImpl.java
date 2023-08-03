package com.sl.palabras.services.impl;

import com.sl.palabras.entities.Language;
import com.sl.palabras.repositories.LanguageRepository;
import com.sl.palabras.services.LanguageService;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service("userService")
@Transactional(readOnly = true)
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    @PostConstruct
    @Transactional(readOnly = false)
    public void init() {
        var en = languageRepository.findOneByCode("en");
        if (en == null) {
            languageRepository.save(Language.builder()
                    .setId((byte) 1)
                    .setCode("en")
                    .setName("English")
                    .build());
        }

        var es = languageRepository.findOneByCode("es");
        if (es == null) {
            languageRepository.save(Language.builder()
                    .setId((byte) 2)
                    .setCode("es")
                    .setName("Español")
                    .build());
        }
    }
}
