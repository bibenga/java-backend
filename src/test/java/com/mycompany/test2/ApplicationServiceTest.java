package com.mycompany.test2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mycompany.test2.db.Application;
import com.mycompany.test2.db.DictApplicationPlatform;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
// @AutoConfigureDataJpa
@AutoConfigureTestEntityManager
// @DataJpaTest(showSql = false)
// @AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
// @ActiveProfiles("jpa-test") // выключаем SecurityFilterChain в TestApplication
@TestPropertySource(locations = "classpath:application-test.properties")
class ApplicationServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ApplicationService applicationService;

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
        entityManager.persist(ios);
        entityManager.persist(android);
        entityManager.persist(
                Application.builder()
                        .setDictApplicationPlatrorm(ios)
                        .setName("safari")
                        .build());
        entityManager.persist(
                Application.builder()
                        .setDictApplicationPlatrorm(android)
                        .setName("safari")
                        .build());
        entityManager.persist(
                Application.builder()
                        .setDictApplicationPlatrorm(ios)
                        .setName("chrome1ЗЕМЛЯ")
                        .build());
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCount() {
        var count = applicationService.count();
        assertEquals(count, 3);
    }

    @Test
    void testFindOne() {
        var res = applicationService.findOne("IOS", "safari");
        assertNotNull(res);
        assertEquals(res.getName(), "safari");
        assertEquals(res.getDictApplicationPlatrorm().getName(), "IOS");
        assertEquals(res.getDictApplicationPlatrorm().getDisplayName(), "iOS");
    }
}
