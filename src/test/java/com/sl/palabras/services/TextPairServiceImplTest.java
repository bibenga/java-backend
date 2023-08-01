package com.sl.palabras.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mycompany.test2.TestApplication;
import com.sl.palabras.entities.TextPair;
import com.sl.palabras.entities.User;
import com.sl.palabras.repositories.TextPairRepository;
import com.sl.palabras.services.exceptions.UserNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest(classes = TestApplication.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TextPairServiceImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TextPairRepository textPairRepository;

    @Autowired
    private TextPairService textPairService;

    @BeforeEach
    @Transactional
    void setup() {
        var user = User.builder().setUsername("a").build();
        entityManager.persist(user);
        entityManager.persist(TextPair.builder()
                .setUser(user)
                .setTest1("t1")
                .setText2("t2")
                .build());
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testLoadPairs() throws UserNotFoundException {
        assertEquals(textPairRepository.count(), 1);
        textPairService.loadPairs("", "a", true);
        assertEquals(textPairRepository.count(), 0);
    }
}
