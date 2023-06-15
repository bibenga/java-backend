package com.mycompany.test2.db;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

// RevisionRepository<Subscription, UUID, Long>
@Repository
public interface SubscriptionRepository
        extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription>, SubscriptionRepositoryExt {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Subscription getOneById(UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Subscription> findById(UUID id);    

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Subscription getOneByAlias(String alias);
}
