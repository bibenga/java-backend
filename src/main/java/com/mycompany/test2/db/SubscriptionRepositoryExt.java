package com.mycompany.test2.db;

import java.util.stream.Stream;

/**
 * Some extra methods
 */
public interface SubscriptionRepositoryExt {
    /**
     * Load all active subscriptions
     * 
     * @return active subscriptions
     */
    Stream<Subscription> finaAllActive();
}
