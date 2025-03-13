package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.TrainerRepository;
import org.epam.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if trainer with username '{}' exists", username);
        String hql = "SELECT COUNT(t) FROM Trainer t WHERE t.username = :username";
        Long count = entityManager.createQuery(hql, Long.class).setParameter("username", username).getSingleResult();
        boolean exists = count != null && count > 0;
        log.info("Trainer with username '{}' exists: {}", username, exists);
        return exists;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        log.debug("Fetching trainer by username '{}'", username);
        Optional<Trainer> trainer = entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class).setParameter("username", username).getResultStream().findFirst();
        if (trainer.isPresent()) {
            log.info("Trainer with username '{}' found", username);
        } else {
            log.warn("No trainer found with username '{}'", username);
        }
        return trainer;
    }

    @Override
    public Trainer create(Trainer trainer) {
        log.info("Creating new trainer: {}", trainer);
        entityManager.persist(trainer);
        log.debug("Trainer '{}' created successfully", trainer.getUsername());
        return trainer;
    }

    @Override
    public Trainer changePassword(Trainer trainer, String password) {
        log.info("Changing password for trainer '{}'", trainer.getUsername());
        trainer.setPassword(password);
        Trainer updated = entityManager.merge(trainer);
        log.debug("Password changed successfully for trainer '{}'", trainer.getUsername());
        return updated;
    }

    @Override
    public Trainer update(Trainer trainer) {
        log.info("Updating trainer: {}", trainer);
        Trainer updated = entityManager.merge(trainer);
        log.debug("Trainer '{}' updated successfully", trainer.getUsername());
        return updated;
    }

    @Override
    public void switchActivate(String username) {
        log.info("Toggling activation status for trainer '{}'", username);
        String jpql = """
                UPDATE Trainer t 
                SET t.isActive = CASE WHEN t.isActive = true THEN false ELSE true END 
                WHERE t.username = :username
                """;
        int updatedRows = entityManager.createQuery(jpql).setParameter("username", username).executeUpdate();
        if (updatedRows > 0) {
            log.info("Activation status toggled for trainer '{}'", username);
        } else {
            log.warn("No trainer found to toggle activation for '{}'", username);
        }
    }
}
