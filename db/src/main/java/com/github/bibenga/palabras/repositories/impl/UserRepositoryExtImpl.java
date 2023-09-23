package com.github.bibenga.palabras.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.bibenga.palabras.entities.User;
import com.github.bibenga.palabras.entities.User_;
import com.github.bibenga.palabras.repositories.UserRepositoryExt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserRepositoryExtImpl implements UserRepositoryExt {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public User getOrCreateOrUpdate(String externalId) {
        log.info("get or create or update user: {}", externalId);
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(User.class);
        var root = query.from(User.class);
        query.where(criteriaBuilder.equal(root.get(User_.externalId), externalId));
        try {
            var user = entityManager.createQuery(query)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult();
            log.info("found user: {}", user.getId());
            // user.setEnabled(!user.isEnabled());
            // user.setEmail(user.getExternalId() + "-1@gmail.com");
            // entityManager.merge(user);
            // entityManager.flush();
            return user;
        } catch (NoResultException e) {
            var user = User.builder()
                    .setExternalId(externalId)
                    .setEnabled(true)
                    .build();
            entityManager.persist(user);
            entityManager.flush();
            return user;
        }
    }
}
