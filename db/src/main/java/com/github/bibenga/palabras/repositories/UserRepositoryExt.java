package com.github.bibenga.palabras.repositories;

import com.github.bibenga.palabras.entities.User;

public interface UserRepositoryExt {
    User findOrCreateOrUpdate(String externalId);
}
