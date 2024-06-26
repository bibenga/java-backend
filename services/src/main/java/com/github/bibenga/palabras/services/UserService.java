package com.github.bibenga.palabras.services;

import com.github.bibenga.palabras.entities.User;

public interface UserService {
    public void addDefaultUsers();

    public User findOneByUsername(final String username);

    public User create(final String username, final String password);

    public User login(final String username, final String password);
}
