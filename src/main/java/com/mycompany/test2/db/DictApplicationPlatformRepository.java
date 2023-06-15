package com.mycompany.test2.db;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface DictApplicationPlatformRepository extends
                JpaRepository<DictApplicationPlatform, Long>,
                JpaSpecificationExecutor<DictApplicationPlatform> {

        @Cacheable("DictApplicationPlatform")
        DictApplicationPlatform getReferenceByName(String name);
}
