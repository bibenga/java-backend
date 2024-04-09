package com.github.bibenga.palabras.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.bibenga.palabras.entities.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Byte>, JpaSpecificationExecutor<Language> {
    Language findOneByCode(String code);

    boolean existsByCode(String code);
}
