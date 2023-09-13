package com.github.bibenga.palabras.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.bibenga.palabras.entities.StudyState;
import com.github.bibenga.palabras.entities.User;

@Repository
public interface StudyStateRepository extends JpaRepository<StudyState, Long>, JpaSpecificationExecutor<StudyState> {
    void deleteAllByTextPairUser(User user);
}
