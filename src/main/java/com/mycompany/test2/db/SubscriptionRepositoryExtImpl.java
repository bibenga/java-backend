package com.mycompany.test2.db;

import java.util.stream.Stream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

class SubscriptionRepositoryExtImpl implements SubscriptionRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Some method
     * 
     * @see com.mycompany.test2.db.SubscriptionRepositoryExt#finaAllActive()
     */
    @Override
    public Stream<Subscription> finaAllActive() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Subscription.class);
        var root = query.from(Subscription.class);
        // root.fetch(Subscription_.serviceList);
        query.where(root.get(Subscription_.status).in(Status.ACTIVE));
        return entityManager.createQuery(query).getResultStream();
    }

}