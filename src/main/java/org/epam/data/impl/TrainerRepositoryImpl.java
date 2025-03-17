package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.TrainerRepository;
import org.epam.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class TrainerRepositoryImpl extends UserRepositoryImpl implements TrainerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            log.info("Creating new trainee: {}", trainer);
            entityManager.persist(trainer);
            log.debug("Trainee '{}' created successfully", trainer.getUsername());
        } else {
            log.info("Updating trainee: {}", trainer);
            entityManager.merge(trainer);
            log.debug("Trainee '{}' updated successfully", trainer.getUsername());
        }
        return trainer;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        log.debug("Fetching trainee by username '{}'", username);

        try {
            Trainer trainee = entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class).setParameter("username", username).getSingleResult();

            log.info("Trainer with username '{}' found", username);
            return Optional.of(trainee);
        } catch (NoResultException e) {
            log.warn("No trainer found with username '{}'", username);
            return Optional.empty();
        }
    }
}
