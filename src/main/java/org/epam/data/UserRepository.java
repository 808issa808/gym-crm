package org.epam.data;

public interface UserRepository {
    int countByUsernamePrefix(String usernamePrefix);
}
