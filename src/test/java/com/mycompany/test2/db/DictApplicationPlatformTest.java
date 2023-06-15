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
class DictApplicationPlatformTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DictApplicationPlatformRepository dictApplicationPlatformRepository;

    @BeforeEach
    @Transactional
    void setup() {
        var ios = DictApplicationPlatform.builder()
                .setId((short) 1)
                .setName("IOS")
                .setDisplayName("iOS")
                .build();
        var android = DictApplicationPlatform.builder()
                .setId((short) 2)
                .setName("ANDROID")
                .setDisplayName("Android")
                .build();
        dictApplicationPlatformRepository.saveAll(Arrays.asList(ios, android));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCount() {
        assertNotNull(entityManager);
        assertNotNull(dictApplicationPlatformRepository);
        assertEquals(dictApplicationPlatformRepository.count(), 2);
    }

    @Test
    void testGetReferenceByName() {
        var ios = dictApplicationPlatformRepository.getReferenceByName("IOS");
        assertNotNull(ios);
        assertEquals(ios.getName(), "IOS");

        var android = dictApplicationPlatformRepository.getReferenceByName("ANDROID");
        assertNotNull(android);
        assertEquals(android.getName(), "ANDROID");

        var windows = dictApplicationPlatformRepository.getReferenceByName("WINDOWS");
        assertNull(windows);
    }

    @Test
    void testUpdate() {
        var windows = DictApplicationPlatform.builder()
                .setId((short) 3)
                .setName("WINDOWS")
                .setDisplayName("Windows")
                .build();
        dictApplicationPlatformRepository.save(windows);
        entityManager.flush();

        windows.setDisplayName("Microsoft Windows");
        dictApplicationPlatformRepository.save(windows);
        entityManager.flush();

        windows.softDelete();
        dictApplicationPlatformRepository.save(windows);
        entityManager.flush();
    }
}
