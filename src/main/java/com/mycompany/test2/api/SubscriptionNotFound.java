package com.mycompany.test2.api;

import java.util.UUID;

// @ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SubscriptionNotFound extends RuntimeException {
    public SubscriptionNotFound() {
        super();
    }

    public SubscriptionNotFound(UUID subscriptionId) {
        super(String.format("subscription with id '%s' not found", subscriptionId));
    }

    public SubscriptionNotFound(String alias) {
        super(String.format("subscription with alias '%s' not found", alias));
    }
}