package com.sl.palabras.services.impl;

import com.sl.palabras.entities.User;
import com.sl.palabras.repositories.StudyStateRepository;
import com.sl.palabras.repositories.TextPairRepository;
import com.sl.palabras.repositories.UserRepository;
import com.sl.palabras.services.TextPairService;
import com.sl.palabras.services.exceptions.UserNotFoundException;

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

    @Override
    @Transactional(readOnly = false)
    public void loadPairs(String filename, String username, boolean override) throws UserNotFoundException {
        var user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        loadPairs(filename, user, override);
    }

    @Override
    @Transactional(readOnly = false)
    public void loadPairs(String filename, User user, boolean override) {
        log.info("load pairs from '{}' with override is {} for user '{}'", filename, override, user);
        if (override) {
            studyStateRepository.deleteAllByTextPairUser(user);
            textPairRepository.deleteAllByUser(user);
        }
        log.error("Unimplemented method 'loadPairs'");
    }
}
