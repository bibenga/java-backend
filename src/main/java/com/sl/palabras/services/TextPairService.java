package com.sl.palabras.services;

import com.sl.palabras.entities.User;

public interface TextPairService {
    public void loadPairs(String filename, String username, boolean override);
    public void loadPairs(String filename, User user, boolean override);
}
