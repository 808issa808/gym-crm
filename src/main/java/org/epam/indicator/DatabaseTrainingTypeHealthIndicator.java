package org.epam.indicator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTrainingTypeHealthIndicator implements HealthIndicator {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Health health() {
        try {
            entityManager.createQuery("SELECT 1 FROM TrainingType tt").getResultList();
            return Health.up().withDetail("Database table TrainingType", "Up and running").build();
        } catch (Exception e) {
            return Health.down().withDetail("Database table TrainingType", "Down").withException(e).build();
        }
    }
}
