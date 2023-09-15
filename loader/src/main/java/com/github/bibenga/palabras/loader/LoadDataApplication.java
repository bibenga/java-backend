package com.github.bibenga.palabras.loader;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.github.bibenga.palabras.loader.services.LanguageService;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EntityScan({ "com.github.bibenga.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.github.bibenga.palabras.repositories" })
@ComponentScan({ "com.github.bibenga.palabras.loader.services.impl" })
@EnableJpaAuditing
@Log4j2
public class LoadDataApplication {

    public static void main(String[] args) {
        // var context = SpringApplication.run(LoadDataApplication.class, args);
        var context = new SpringApplicationBuilder(LoadDataApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        var environment = context.getEnvironment();
        try {
            var addLanguages = environment.getProperty(
                    "com.github.bibenga.palabras.add-languages",
                    boolean.class,
                    false);
            log.info("should add languages: {}", addLanguages);
            if (addLanguages == Boolean.TRUE) {
                var languageService = context.getBean(LanguageService.class);
                languageService.addDefaultLanguages();
            }
        } catch (Exception e) {
            log.catching(e);
        }
        context.stop();
    }
}