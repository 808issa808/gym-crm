package org.epam.indicator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUserHealthIndicator implements HealthIndicator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Health health() {
        try {
            entityManager.createQuery("SELECT 1 FROM User u").getResultList();
            return Health.up().withDetail("Database table User", "Up and running").build();
        } catch (Exception e) {
            return Health.down().withDetail("Database table User", "Down").withException(e).build();
        }
    }
}