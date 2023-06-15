package com.mycompany.test2.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Service getOneByInputTopicAndStatusIn(String inputTopic, Collection<Status> statusList);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(
            //"jakarta.persistence.lock.timeout",LockOptions.NO_WAIT
            value = {@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")},
            forCounting = false
    )
    List<Service> findAllBySubscriptionIsNullAndStatus(Status status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Service> findAllBySubscriptionIsNotNullAndStatus(Status status, Pageable pageable);

    List<Service> findAllByInputTopicIn(Collection<String> inputTopicList);
}
