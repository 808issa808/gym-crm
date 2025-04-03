package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.UserRepository;
import org.epam.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countByUsernamePrefix(String usernamePrefix) {
        log.debug("Counting users with username starting with '{}'", usernamePrefix);

        String hql = "SELECT COUNT(u) FROM User u WHERE u.username LIKE :usernamePrefix";
        Long count = entityManager.createQuery(hql, Long.class).setParameter("usernamePrefix", usernamePrefix + "%").getSingleResult();

        int result = count != null ? count.intValue() : 0;
        log.info("Number of users with username starting with '{}': {}", usernamePrefix, result);

        return result;
    }
    @Override
    public Optional<User> findByUsernameUser(String username) {
        log.debug("Fetching trainee by username '{}'", username);

        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username).getSingleResult();

            log.info("Trainee with username '{}' found", username);
            return Optional.of(user);
        } catch (NoResultException e) {
            log.warn("No trainee found with username '{}'", username);
            return Optional.empty();
        }
    }
}
