package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.TraineeRepository;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TraineeRepositoryImpl extends UserRepositoryImpl implements TraineeRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.debug("Fetching trainee by username '{}'", username);

        try {
            Trainee trainee = entityManager.createQuery("SELECT t FROM Trainee t WHERE t.username = :username", Trainee.class).setParameter("username", username).getSingleResult();

            log.info("Trainee with username '{}' found", username);
            return Optional.of(trainee);
        } catch (NoResultException e) {
            log.warn("No trainee found with username '{}'", username);
            return Optional.empty();
        }
    }

    @Override
    public List<Trainer> getNotMineTrainersByUsername(String username) {
        log.debug("Fetching trainers not assigned to trainee with username '{}'", username);
        String hql = """
                SELECT tr FROM Trainer tr
                           WHERE tr.isActive = true
                           AND NOT EXISTS (
                               SELECT 1 FROM Trainee tn
                               JOIN tn.trainers t
                               WHERE t = tr AND tn.username = :username
                           )
                """;
        List<Trainer> trainers = entityManager.createQuery(hql, Trainer.class).setParameter("username", username).getResultList();
        log.info("Found {} trainers not assigned to trainee '{}'", trainers.size(), username);
        return trainers;
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            log.info("Creating new trainee: {}", trainee);
            entityManager.persist(trainee);
            log.debug("Trainee '{}' created successfully", trainee.getUsername());
        } else {
            log.info("Updating trainee: {}", trainee);
            entityManager.merge(trainee);
            log.debug("Trainee '{}' updated successfully", trainee.getUsername());
        }
        return trainee;
    }

    @Override
    public void delete(Trainee trainee) {
        log.info("Deleting trainee with username '{}'", trainee.getUsername());

        Trainee existingTrainee = entityManager.find(Trainee.class, trainee.getId());
        if (existingTrainee != null) {
            entityManager.remove(existingTrainee);
            log.info("Trainee '{}' deleted successfully", trainee.getUsername());
        } else {
            log.warn("No trainee found with username '{}' to delete", trainee.getUsername());
        }
    }
}