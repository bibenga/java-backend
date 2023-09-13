package com.github.bibenga.palabras.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.bibenga.palabras.repositories.LanguageRepository;

import lombok.extern.log4j.Log4j2;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/language")
@Log4j2
@Transactional(readOnly = true)
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping(produces = { "application/json" })
    @ResponseBody
    public Page<LanguageDTO> getLanguages(Principal principal) {
        var dbLangs = languageRepository.findAll();
        var respLangs = new ArrayList<LanguageDTO>(dbLangs.size());
        for (var dbLang : dbLangs) {
            respLangs.add(LanguageDTO.builder()
                    .setId(dbLang.getId())
                    .setCode(dbLang.getCode())
                    .setName(dbLang.getName())
                    .build());
        }
        log.info("find {} langs: {}", respLangs.size(), respLangs);
        return new PageImpl<>(respLangs);
    }

    @GetMapping(path="/{id}", produces = { "application/json" })
    @ResponseBody
    public LanguageDTO getOne(@PathVariable int id, Principal principal) throws LanguageNotFoundException {
        log.info("try find a language with id: {}", id);
        var dbLang = languageRepository.findById((byte) id);
        if (!dbLang.isPresent()) {
            log.info("a language with id {} is not found", id);
            throw new LanguageNotFoundException(id);
        }
        var respLang = LanguageDTO.builder()
                    .setId(dbLang.get().getId())
                    .setCode(dbLang.get().getCode())
                    .setName(dbLang.get().getName())
                    .build();
        log.info("the language with id {} is: {}", id, respLang);
        return respLang;
    }
}
