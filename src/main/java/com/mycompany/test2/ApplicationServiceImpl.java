package com.mycompany.test2;

import com.mycompany.test2.db.Application;
import com.mycompany.test2.db.ApplicationRepository;
import com.mycompany.test2.db.Application_;
import com.mycompany.test2.db.DictApplicationPlatform_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service("applicationService")
@Log4j2
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    @Cacheable(cacheNames = "Application")
    public long count() {
        return applicationRepository.count();
    }

    @Override
    @Cacheable(value = "Application")
    public Collection<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(value = "Application")
    @CacheEvict(value = "Application", allEntries = true)
    public Application save(Application application) {
        log.info("save application - {}", application);
        applicationRepository.save(application);
        return application;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Application")
    public Application findOne(final String dictApplicationPlatrorm, final String name) {
        log.info("try find application '{}' for '{}'", name, dictApplicationPlatrorm);
        var application = applicationRepository.findOne(new Specification<Application>() {
            @Override
            public Predicate toPredicate(Root<Application> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                root.fetch(Application_.dictApplicationPlatrorm);
                var dictApplicationPlatrormTable = root.get(Application_.dictApplicationPlatrorm);
                return cb.and(
                        cb.equal(dictApplicationPlatrormTable.get(DictApplicationPlatform_.name),
                                dictApplicationPlatrorm),
                        // cb.not(root.get(Application_.softDeleted)),
                        cb.isNull(root.get(Application_.deleted)),
                        cb.equal(root.get(Application_.name), name));
            }
        });
        return application.get();
    }

}
