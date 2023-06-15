package com.mycompany.test2.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionStatusRepository extends JpaRepository<SubscriptionStatus, Long>, JpaSpecificationExecutor<SubscriptionStatus> {
}
