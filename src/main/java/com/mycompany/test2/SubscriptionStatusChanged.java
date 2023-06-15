package com.mycompany.test2;

import com.mycompany.test2.db.Status;
import com.mycompany.test2.db.Subscription;
import org.springframework.context.ApplicationEvent;

public class SubscriptionStatusChanged extends ApplicationEvent {
    private final Status previousStatus;

    public SubscriptionStatusChanged(Subscription subscription) {
        super(subscription);
        this.previousStatus = null;
    }

    public SubscriptionStatusChanged(Subscription subscription, Status previousStatus) {
        super(subscription);
        this.previousStatus = previousStatus;
    }

    public Subscription getSubscription() {
        assert getSource() instanceof Subscription;
        return (Subscription) getSource();
    }

    public Status getPreviousStatus() {
        return previousStatus;
    }
}
