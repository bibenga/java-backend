package com.mycompany.test2.api;

import java.util.UUID;

// @ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SubscriptionAlreadyExists extends RuntimeException {
    public SubscriptionAlreadyExists() {
        super();
    }

    public SubscriptionAlreadyExists(UUID subscriptionId) {
        super(String.format("subscription %s already exists", subscriptionId));
    }
}