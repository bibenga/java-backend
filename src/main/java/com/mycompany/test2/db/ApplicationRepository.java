package com.mycompany.test2.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    // Application findOneByDictApplicationPlatrormAndName(
    //     String dictApplicationPlatrormName,
    //     String name
    // );
}
