package com.sl.palabras.services.impl;

import com.sl.palabras.entities.User;
import com.sl.palabras.repositories.StudyStateRepository;
import com.sl.palabras.repositories.TextPairRepository;
import com.sl.palabras.repositories.UserRepository;
import com.sl.palabras.services.TextPairService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service("textPairService")
@Log4j2
@Transactional(readOnly = true)
public class TextPairServiceImpl implements TextPairService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TextPairRepository textPairRepository;

    @Autowired
    private StudyStateRepository studyStateRepository;

    @PostConstruct
    public void init() {
        loadPairs("", "a", true);
    }

    @Override
    public void loadPairs(String filename, String username, boolean override) {
        var user = userRepository.findOneByUsername(username);
        loadPairs(filename, user, override);
    }

    @Override
    public void loadPairs(String filename, User user, boolean override) {
        log.info("load pairs from '{}' with override is {} for user '{}'", filename, override, user);
        if (override) {
            studyStateRepository.deleteAllByTextPairUser(user);
            textPairRepository.deleteAllByUser(user);
            // studyStateRepository.delete(new Specification<StudyState>() {
            //     @Override
            //     @Nullable
            //     public Predicate toPredicate(Root<StudyState> root, CriteriaQuery<?> query,
            //             CriteriaBuilder criteriaBuilder) {
            //         throw new UnsupportedOperationException("Unimplemented method 'toPredicate'");
            //     }
            // });
        }
        log.error("Unimplemented method 'loadPairs'");
    }
}
