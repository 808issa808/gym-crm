package org.epam.util;

import org.epam.model.User;
import org.epam.web.exp.AuthException;

import java.util.Optional;
import java.util.function.Function;

public class Authenticator {
    public static <T extends User> T authenticateUser(String username, String password, Function<String, Optional<T>> findByUsername) {
        return findByUsername.apply(username)
                .filter(x -> x.getPassword().equals(password))
                .orElseThrow(() -> new AuthException("wrong username or password"));
    }
}
