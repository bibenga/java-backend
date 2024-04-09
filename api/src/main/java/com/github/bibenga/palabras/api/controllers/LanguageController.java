package com.github.bibenga.palabras.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.github.bibenga.palabras.repositories.LanguageRepository;
import lombok.extern.log4j.Log4j2;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/language", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Log4j2
@Transactional(readOnly = true)
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundError(final Throwable e, Principal principal) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(detail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleError(final Throwable e, Principal principal) {
        log.error("a", e);
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(detail);
    }

    @GetMapping()
    @ResponseBody
    public Page<LanguageDTO> getLanguages(Pageable page, Principal principal) {
        log.info("page is {}", page);
        log.info("user is {}", principal == null ? "anonymous" : principal.getName());
        var dbLangs = languageRepository.findAll(page);
        log.info("loaded langs: {}", dbLangs);
        var respLangs = dbLangs.getContent().stream().map(db -> new LanguageDTO(db)).collect(Collectors.toList());
        log.info("find {} langs: {}", respLangs.size(), respLangs);
        return new PageImpl<>(respLangs, page, dbLangs.getTotalElements());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public LanguageDTO getOne(@PathVariable int id) throws LanguageNotFoundException {
        log.info("try find a language with id: {}", id);
        var dbLang = languageRepository.findById((byte) id).orElseThrow(() -> new LanguageNotFoundException(id));
        var respLang = new LanguageDTO(dbLang);
        log.info("the language with id {} is: {}", id, respLang);
        return respLang;
    }
}
