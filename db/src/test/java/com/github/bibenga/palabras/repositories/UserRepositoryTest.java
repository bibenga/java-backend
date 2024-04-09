package com.github.bibenga.palabras.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testFindOrCreateOrUpdate() {
        var externalId = "123";
        assertEquals(userRepository.count(), 0);
        var user = userRepository.findOrCreateOrUpdate(externalId);
        assertNotNull(user);
        assertEquals(userRepository.count(), 1);
        entityManager.clear(); // clear cache for load new instance of object
        var user2 = userRepository.findOneByExternalId(externalId);
        assertEquals(user.getId(), user2.getId());
    }
}
