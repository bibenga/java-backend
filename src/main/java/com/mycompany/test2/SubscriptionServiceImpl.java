package com.mycompany.test2;

import com.mycompany.test2.api.SubscriptionNotFound;
import com.mycompany.test2.db.Status;
import com.mycompany.test2.db.Subscription;
import com.mycompany.test2.db.SubscriptionRepository;
import com.mycompany.test2.db.SubscriptionStatus;
import com.mycompany.test2.db.SubscriptionStatusRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;

@Service("subscriptionService")
@Log4j2
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionStatusRepository subscriptionStatusRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public long count() {
        var count = subscriptionRepository.count();
        log.info("subscriptions count: {}", count);
        return count;
    }

    @Transactional(readOnly = true)
    public boolean exists(String alias) {
        var subscription = new Subscription();
        subscription.setAlias(alias);
        return subscriptionRepository.exists(Example.of(subscription));
    }

    @Transactional(readOnly = true)
    public Subscription getById(UUID id) {
        var subscription = subscriptionRepository.getReferenceById(id);
        if (subscription == null) {
            log.info("subscription {} not found", id);
            throw new SubscriptionNotFound(id);
            // return null;
        }
        try {
            // только при вызове метода реально выдасться EntityNotFoundException
            subscription.getAlias(); 
        } catch (EntityNotFoundException e) {
            log.info("subscription {} not found", id);
            throw new SubscriptionNotFound(id);
            // return null;
        }
        log.info("subscription loaded -> {}", subscription);
        return subscription;
    }

    @Transactional(readOnly = true)
    public Subscription getByAlias(String alias) {
        var subscription =  subscriptionRepository.getOneByAlias(alias);
        if (subscription == null) {
            throw new SubscriptionNotFound(alias);
        }
        return subscription;
    }

    @Transactional
    public void create(Subscription subscription) {
        if (subscription.getId() != null) {
            throw new RuntimeException("id should be null");
        }
        subscription.setId(UUID.randomUUID());
        subscription.setStatus(Status.READY);
        subscription.setStatusDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);

        var subscriptionStatus = new SubscriptionStatus();
        subscriptionStatus.setSubscription(subscription);
        subscriptionStatus.setStatus(subscription.getStatus());
        subscriptionStatus.setStatusDate(subscription.getStatusDate());
        subscriptionStatusRepository.save(subscriptionStatus);

        applicationEventPublisher.publishEvent(new SubscriptionStatusChanged(subscription, null));
    }

    @Transactional
    public void move(Subscription subscription, Status newStatus) {
        if (subscription.getStatus() != newStatus) {
            var previousStatus = subscription.getStatus();

            subscription.setStatus(newStatus);
            subscription.setStatusDate(ZonedDateTime.now());
            subscriptionRepository.save(subscription);

            var subscriptionStatus = new SubscriptionStatus();
            subscriptionStatus.setSubscription(subscription);
            subscriptionStatus.setStatus(subscription.getStatus());
            subscriptionStatus.setStatusDate(subscription.getStatusDate());
            subscriptionStatusRepository.save(subscriptionStatus);

            applicationEventPublisher.publishEvent(new SubscriptionStatusChanged(subscription, previousStatus));
        }
    }

    @Transactional
    public void save(Subscription subscription) {
        subscriptionRepository.save(subscription);

        // if (subscription.getPreviousStatus() != null) {
        //     applicationEventPublisher.publishEvent(new SubscriptionStatusChanged(subscription));
        // }
    }

    // @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("tik: subscriptions={}", subscriptionRepository.count());
    }
}
