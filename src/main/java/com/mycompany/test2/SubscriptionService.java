package com.mycompany.test2;

import com.mycompany.test2.db.Status;
import com.mycompany.test2.db.Subscription;
import java.util.UUID;

public interface SubscriptionService {
    public boolean exists(String alias);

    public long count();

    public Subscription getById(UUID id);

    public Subscription getByAlias(String alias);

    public void create(Subscription subscription);

    public void move(Subscription subscription, Status newStatus);

    public void save(Subscription subscription);
}
