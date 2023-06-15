package com.mycompany.test2.db;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;

// @SpringBootTest
// @Transactional
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("jpa-test") // выключаме SecurityFilterChain в TestApplication
@TestPropertySource(locations = "classpath:application-test.properties")
@Log4j2
class SubscriptionRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    // @Autowired
    // private TransactionTemplate tx;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    // @BeforeEach
    // void setup() {
    // subscriptionRepository.deleteAll();
    // }

    @Test
    void testRepositoryWithDb() {
        assertNotNull(entityManager);
        assertNotNull(subscriptionRepository);
        assertEquals(subscriptionRepository.count(), 0);
        try (var allActive = subscriptionRepository.finaAllActive()) {
            // assertThat(subscriptionRepository.finaAllActive().count()).isEqualTo(0);
            assertEquals(allActive.count(), 0);
        }
    }

    @Test
    void testSave() {
        var subscription = Subscription.builder()
                .setAlias("1")
                .setStatus(Status.READY)
                .setStatusDate(ZonedDateTime.now())
                .build();

        log.info("subscription: {}", subscription);
        subscriptionRepository.save(subscription);
        log.info("subscription: {}", subscription);

        assertEquals(subscriptionRepository.count(), 1);
        assertTrue(subscriptionRepository.exists(
                Example.of(Subscription.builder().setAlias("1").build())));

        subscription.setStatus(Status.ERROR);
        // subscription.setStatusDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);
        assertEquals(subscriptionRepository.count(), 1);
    }

    @Test
    void testLock() {
        // var subscriptoinId = UUID.randomUUID();
        var subscription = new Subscription();
        // subscription.setId(subscriptoinId);
        subscription.setAlias("1");
        subscription.setStatus(Status.READY);
        subscription.setStatusDate(ZonedDateTime.now());
        // entityManager.persist(subscription);
        subscriptionRepository.save(subscription);
        assertEquals(subscriptionRepository.count(), 1);

        assertNotNull(subscription.getId());
        // assertEquals(subscription.getId()).isEqualTo(subscriptoinId);
        var subscriptoinId = subscription.getId();

        var subscriprion2 = subscriptionRepository.getOneByAlias("1");
        assertNotNull(subscriprion2);
        assertEquals(subscriprion2.getId(), subscriptoinId);
    }

    @Test
    void testLock_tx() {
        var tx = new TransactionTemplate(platformTransactionManager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);

        UUID subscriptoinId = tx.execute(status -> {
            var subscription = new Subscription();
            // subscription.setId(subscriptoinId);
            subscription.setAlias("1");
            subscription.setStatus(Status.READY);
            subscription.setStatusDate(ZonedDateTime.now());
            entityManager.persist(subscription);
            // entityManager.flush();
            // entityManager.clear();
            return subscription.getId();
        });
        assertNotNull(subscriptoinId);

        assertEquals(subscriptionRepository.count(), 1);

        tx.execute(status -> {
            var subscriprion2 = subscriptionRepository.getOneByAlias("1");
            assertNotNull(subscriprion2);
            // assertThat(subscriprion2.getId()).isEqualByComparingTo(subscriptoinId);
            assertEquals(subscriprion2.getId(), subscriptoinId);
            return null;
        });
    }

    @Test
    void testFinaAllActive() {
        var subscription = Subscription.builder()
                .setAlias("1")
                .setStatus(Status.READY)
                .setStatusDate(ZonedDateTime.now())
                .build();
        subscriptionRepository.save(subscription);

        var active = subscriptionRepository.finaAllActive().toList();
        assertEquals(active.size(), 0);

        subscription.setStatus(Status.ACTIVE);
        subscription.setStatusDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);

        active = subscriptionRepository.finaAllActive().toList();
        assertEquals(active.size(), 1);
    }

}
