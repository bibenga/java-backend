package com.github.bibenga.palabras.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.github.bibenga.palabras.loader.services.LanguageService;

@SpringBootApplication
@ComponentScan({ "com.github.bibenga.palabras.loader.services.impl" })
@EntityScan({ "com.github.bibenga.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.github.bibenga.palabras.repositories" })
@EnableJpaAuditing
public class LoadDataApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(LoadDataApplication.class, args);
        try {
            var languageService = context.getBean(LanguageService.class);
            languageService.addDefaultLanguages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.stop();
    }
}