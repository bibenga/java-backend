package com.github.bibenga.palabras.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.bibenga.palabras.entities.TextPair;
import com.github.bibenga.palabras.entities.User;

@Repository
public interface TextPairRepository extends JpaRepository<TextPair, Long>, JpaSpecificationExecutor<TextPair> {
    void deleteAllByUser(User user);
}
