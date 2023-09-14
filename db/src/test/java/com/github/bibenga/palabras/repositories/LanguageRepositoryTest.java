package com.github.bibenga.palabras.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.github.bibenga.palabras.entities.Language;

@DataJpaTest(showSql = false)
// @ContextConfiguration(classes=TestConfiguration.class)
@TestPropertySource(locations = { "classpath:application-test.properties" })
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testSave() {
        // test for "play" with test
        var lang = Language.builder()
                .setId((byte) 1)
                .setCode("en")
                .setName("English")
                .build();
        languageRepository.save(lang);
        assertEquals(languageRepository.count(), 1L);
    }
}
