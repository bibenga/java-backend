package com.sl.palabras.services.impl;

import com.sl.palabras.entities.User;
import com.sl.palabras.repositories.UserRepository;
import com.sl.palabras.services.UserService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service("userService")
@Log4j2
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    @Transactional(readOnly = false)
    public void init() {
        var user = userRepository.findOneByUsername("a");
        if (user == null) {
            log.warn("Demo user crated");
            userRepository.save(User.builder().setUsername("a").build());
        }
    }

    @Override
    public User findOneByUsername(String username) {
        var user = userRepository.findOneByUsername(username);
        if (user == null) {
            
        }
        log.info("User '{}' loaded: {}", username, user);
        return user;
    }

    @Transactional(readOnly = false)
    @Override
    public User create(String username) {
        var user = User.builder().setUsername(username).build();
        userRepository.save(user);
        userRepository.flush();
        log.info("User '{}' created: {}", username, user);
        return user;
    }
}
