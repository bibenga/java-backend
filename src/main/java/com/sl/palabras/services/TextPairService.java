package com.sl.palabras.services;

import com.sl.palabras.entities.User;
import com.sl.palabras.services.exceptions.UserNotFoundException;

public interface TextPairService {
    public void loadPairs(String filename, String username, boolean override) throws UserNotFoundException;

    public void loadPairs(String filename, User user, boolean override);
}
