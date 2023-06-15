package com.mycompany.test2.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("jpa-test") // выключаем SecurityFilterChain в TestApplication
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class DictServiceTypeRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DictServiceTypeRepository dictServiceTypeRepository;

    @BeforeEach
    @Transactional
    void setup() {
        var syncType = DictServiceType.builder()
                .setId((short) 1)
                .setName("SYNC")
                .setDisplayName("SYNC: ПОПА, 😃, 2⁸, é!")
                .build();
        var streamType = DictServiceType.builder()
                .setId((short) 2)
                .setName("STREAM")
                .setDisplayName("STREAM")
                .build();
        dictServiceTypeRepository.saveAll(Arrays.asList(syncType, streamType));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCount() {
        assertNotNull(entityManager);
        assertNotNull(dictServiceTypeRepository);
        assertEquals(dictServiceTypeRepository.count(), 2);
    }

    @Test
    void testGetReferenceByName() {
        var syncType = dictServiceTypeRepository.getReferenceByName("SYNC");
        assertNotNull(syncType);
        assertEquals(syncType.getName(), "SYNC");

        var streamType = dictServiceTypeRepository.getReferenceByName("STREAM");
        assertNotNull(streamType);
        assertEquals(streamType.getName(), "STREAM");

        var someType = dictServiceTypeRepository.getReferenceByName("SOME");
        assertNull(someType);

        var syncType2 = dictServiceTypeRepository.getReferenceByName("SYNC");
        assertNotNull(syncType2);
        // assertEquals(syncType != syncType2, true);
    }
}
