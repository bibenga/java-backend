package com.mycompany.test2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.mycompany.test2.api.SubscriptionNotFound;
import com.mycompany.test2.db.Status;
import com.mycompany.test2.db.Subscription;
import com.mycompany.test2.db.SubscriptionRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    private static final UUID SUBSCRIPRION_1 = UUID.randomUUID();
    private static final UUID SUBSCRIPRION_2 = UUID.randomUUID();

    @BeforeEach
    void setup() {
        var subscription = new Subscription();
        subscription.setId(SUBSCRIPRION_1);
        subscription.setAlias("1");
        subscription.setStatus(Status.READY);
        subscription.setCreatedDate(ZonedDateTime.now());
        subscription.setModifiedDate(ZonedDateTime.now());

        Mockito.when(subscriptionRepository.count()).thenReturn(1L);
        Mockito.when(subscriptionRepository.getReferenceById(SUBSCRIPRION_1)).thenReturn(subscription);
        Mockito.when(subscriptionRepository.getOneByAlias("1")).thenReturn(subscription);
    }

    @Test
    void testMockedRepository() {
        assertNotNull(subscriptionService);
        assertEquals(subscriptionService.count(), 1L);

        var subscription = subscriptionService.getById(SUBSCRIPRION_1);
        assertNotNull(subscription);
        assertEquals(subscription.getId(), SUBSCRIPRION_1);

        subscription = subscriptionService.getByAlias("1");
        assertNotNull(subscription);
        assertEquals(subscription.getId(), SUBSCRIPRION_1);
        assertEquals(subscription.getAlias(), "1");
    }

    @Test
    void tesNotFoundById() {
        var error = assertThrows(SubscriptionNotFound.class, () -> {
            subscriptionService.getById(SUBSCRIPRION_2);
        });
        assertEquals(error.getMessage(),
                String.format("subscription with id '%s' not found", SUBSCRIPRION_2));
    }

    @Test
    void tesNotFoundByAlias() {
        var error = assertThrows(SubscriptionNotFound.class, () -> {
            subscriptionService.getByAlias("2");
        });
        assertEquals(error.getMessage(), "subscription with alias '2' not found");
    }
}
