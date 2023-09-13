package com.github.bibenga.palabras.services.impl;

import com.github.bibenga.palabras.entities.User;
import com.github.bibenga.palabras.repositories.UserRepository;
import com.github.bibenga.palabras.services.UserService;

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

    @Override
    @Transactional(readOnly = false)
    public void addDefaultUsers() {
        var user = userRepository.findOneByUsername("a");
        if (user == null) {
            create("a", "a");
        } else {
            log.info("User '{}' exists: {}", user.getUsername(), user);
        }
    }

    @Override
    public User findOneByUsername(String username) {
        var user = userRepository.findOneByUsername(username.toLowerCase());
        if (user == null) {

        }
        log.info("User '{}' loaded: {}", username, user);
        return user;
    }

    @Transactional(readOnly = false)
    @Override
    public User create(String username, final String password) {
        var user = User.builder()
                .setUsername(username.toLowerCase())
                .setPassword(password) // muy seguro 🤣
                .build();
        userRepository.save(user);
        userRepository.flush();
        log.info("User '{}' created: {}", username, user);
        return user;
    }

    @Transactional(readOnly = false)
    @Override
    public User login(String username, final String password) {
        // Ja, sí, ¡envío la contraseña al log! 🤣
        // Es seguro porque nadie nunca lee nada en el log. 🤣
        log.info("try to login: username='{}', password='{}'", username, password);
        var user = userRepository.findOneByUsername(username.toLowerCase());
        if (user == null) {
            log.info("User not found");
            throw new NullPointerException(String.format("User %s is disabled", username));
        }
        if (user.getPassword() == null) {
            log.info("User disabled");
            throw new RuntimeException(String.format("User %s is disabled", password));
        }
        if (user.getPassword() != password) { // es seguro tambian 🤣
            log.info("Invalid password");
            throw new RuntimeException(String.format("User %s is disabled", username));
        }
        // check password
        log.info("User '{}' loaded: {}", username, user);
        return user;
    }
}
