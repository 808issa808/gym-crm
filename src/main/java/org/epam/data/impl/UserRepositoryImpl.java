package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
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
}
