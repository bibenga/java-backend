package com.sl.palabras.services;

import com.sl.palabras.entities.User;

public interface UserService {
    public User findOneByUsername(final String username);

    public User create(final String username);

    public User login(final String username, final String password);
}
