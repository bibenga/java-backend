package com.github.bibenga.palabras.services;

import com.github.bibenga.palabras.entities.User;
import com.github.bibenga.palabras.services.exceptions.UserNotFoundException;

public interface TextPairService {
    public void loadPairs(String filename, String username, boolean override) throws UserNotFoundException;

    public void loadPairs(String filename, User user, boolean override);
}
