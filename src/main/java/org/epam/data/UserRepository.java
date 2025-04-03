package org.epam.data;

import org.epam.model.User;

import java.util.Optional;

public interface UserRepository {
    int countByUsernamePrefix(String usernamePrefix);


    Optional<User> findByUsernameUser(String username);
}
