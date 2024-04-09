package com.github.bibenga.palabras.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.wicketstuff.annotation.mount.MountPath;

import com.github.bibenga.palabras.repositories.LanguageRepository;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

@MountPath("admin")
@Log4j2
public class AdminPage extends WebPage {
    @Inject
    private PlatformTransactionManager transactionManager;
    protected TransactionTemplate transactionTemplate;

    @Inject
    private LanguageRepository languageRepository;

    public AdminPage(final PageParameters parameters) {
        super(parameters);
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setReadOnly(true);

        transactionTemplate.executeWithoutResult(t -> {
            log.info("langauges: {}", languageRepository.count());
            log.info("langauges: {}", languageRepository.findAll());
        });
    }
}
