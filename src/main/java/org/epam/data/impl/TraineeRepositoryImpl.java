package org.epam.data.impl;

import jakarta.persistence.EntityManager;
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
public class TraineeRepositoryImpl implements TraineeRepository {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public boolean existsByUsername(String username) {
//        log.debug("Checking if trainee with username '{}' exists", username);
//        String hql = "SELECT COUNT(t) FROM Trainee t WHERE t.username = :username";
//        Long count = entityManager.createQuery(hql, Long.class).setParameter("username", username).getSingleResult();
//        boolean exists = count != null && count > 0;
//        log.info("Trainee with username '{}' exists: {}", username, exists);
//        return exists;
//    }

    @Override
    public int countByUsernamePrefix(String usernamePrefix) {
        log.debug("Counting trainees with username starting with '{}'", usernamePrefix);

        String hql = "SELECT COUNT(u) FROM User u WHERE u.username LIKE :usernamePrefix";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("usernamePrefix", usernamePrefix + "%") // Добавляем % для поиска по началу строки
                .getSingleResult();

        int result = count != null ? count.intValue() : 0;
        log.info("Number of trainees with username starting with '{}': {}", usernamePrefix, result);

        return result;
    }


    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.debug("Fetching trainee by username '{}'", username);
        Optional<Trainee> trainee = entityManager.
                createQuery("SELECT t FROM Trainee t WHERE t.username = :username",
                Trainee.class).
                setParameter("username", username).
                getResultStream().findFirst();

        if (trainee.isPresent()) {
            log.info("Trainee with username '{}' found", username);
        } else {
            log.warn("No trainee found with username '{}'", username);
        }
        return trainee;
    }

    @Override
    public List<Trainer> getNotMineTrainersByUsername(String username) {
        log.debug("Fetching trainers not assigned to trainee with username '{}'", username);
        String hql = """
                SELECT tr FROM Trainer tr 
                WHERE NOT EXISTS (
                    SELECT 1 FROM Trainee tn 
                    JOIN tn.trainers t 
                    WHERE t = tr AND tn.username = :username
                )
                """;
        List<Trainer> trainers = entityManager.createQuery(hql, Trainer.class).setParameter("username", username).getResultList();
        log.info("Found {} trainers not assigned to trainee '{}'", trainers.size(), username);
        return trainers;
    }

//    @Override
//    public Trainee create(Trainee trainee) {
//        log.info("Creating new trainee: {}", trainee);
//        entityManager.persist(trainee);
//        log.debug("Trainee '{}' created successfully", trainee.getUsername());
//        return trainee;
//    }
//
//    @Override
//    public Trainee update(Trainee trainee) {
//        log.info("Updating trainee: {}", trainee);
//        Trainee updated = entityManager.merge(trainee);
//        log.debug("Trainee '{}' updated successfully", trainee.getUsername());
//        return updated;
//    }

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

//    @Override
//    public Trainee changePassword(Trainee trainee, String password) {
//        log.info("Changing password for trainee '{}'", trainee.getUsername());
//        trainee.setPassword(password);
//        Trainee updated = entityManager.merge(trainee);
//        log.debug("Password changed successfully for trainee '{}'", trainee.getUsername());
//        return updated;
//    }
//
//    @Override
//    public Trainee updateTrainersList(Trainee trainee, List<Trainer> updatedTrainers) {
//        log.info("Updating trainer list for trainee '{}'", trainee.getUsername());
//        trainee.setTrainers(updatedTrainers);
//        Trainee updated = entityManager.merge(trainee);
//        log.debug("Trainer list updated successfully for trainee '{}'", trainee.getUsername());
//        return updated;
//    }

//    @Override
//    public void switchActivate(String username) {
//        log.info("Toggling activation status for trainee '{}'", username);
//        String jpql = """
//                UPDATE Trainee t
//                SET t.isActive = CASE WHEN t.isActive = true THEN false ELSE true END
//                WHERE t.username = :username
//                """;
//        int updatedRows = entityManager.createQuery(jpql).setParameter("username", username).executeUpdate();
//        if (updatedRows > 0) {
//            log.info("Activation status toggled for trainee '{}'", username);
//        } else {
//            log.warn("No trainee found to toggle activation for '{}'", username);
//        }
//    }

    @Override
    public void delete(Trainee trainee) {
        log.info("Deleting trainee with username '{}'", trainee.getUsername());

        Trainee existingTrainee = entityManager.find(Trainee.class, trainee.getId());
        if (existingTrainee != null) {
            log.info("Trainee '{}' deleted successfully", trainee.getUsername());
        } else {
            log.warn("No trainee found with username '{}' to delete", trainee.getUsername());
        }
    }

}