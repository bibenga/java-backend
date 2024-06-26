package com.github.bibenga.palabras.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.github.bibenga.palabras.entities.Language;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testSave() {
        // test for "play" with test
        assertEquals(languageRepository.count(), 0);
        var lang = Language.builder()
                .setId((byte) -1)
                .setCode("en")
                .setName("English")
                .build();
        languageRepository.save(lang);
        assertEquals(languageRepository.count(), 1);
    }
}
