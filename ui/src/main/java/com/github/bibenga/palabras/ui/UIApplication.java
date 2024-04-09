package com.github.bibenga.palabras.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
// @EnableAsync
// @EnableScheduling
// @EnableCaching
@EntityScan({ "com.github.bibenga.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.github.bibenga.palabras.repositories" })
@EnableJpaAuditing
// @EnableTransactionManagement
// @ComponentScan({ "com.github.bibenga.palabras.api.controllers" })
@EnableWebMvc
@Log4j2
public class UIApplication {
    public static void main(String[] args) {
        SpringApplication.run(UIApplication.class, args);
    }
}
