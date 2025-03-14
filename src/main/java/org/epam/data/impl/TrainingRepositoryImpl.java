package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.TrainingRepository;
import org.epam.model.Training;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_BY_TRAINEE_CRITERIA = """
                SELECT tr FROM Training tr 
                WHERE tr.trainee.username = :traineeUsername 
                AND (:fromDate IS NULL OR tr.date >= :fromDate) 
                AND (:toDate IS NULL OR tr.date <= :toDate) 
                AND (:trainerName IS NULL OR 
                    LOWER(tr.trainer.lastName) LIKE LOWER(CONCAT('%', :trainerName, '%')) 
                    OR LOWER(tr.trainer.firstName) LIKE LOWER(CONCAT('%', :trainerName, '%'))) 
                AND (:trainingType IS NULL OR LOWER(tr.type.trainingTypeName) = LOWER(:trainingType))
            """;

    private static final String FIND_BY_TRAINER_CRITERIA = """
                SELECT tr FROM Training tr 
                WHERE tr.trainer.username = :trainerUsername 
                AND (:fromDate IS NULL OR tr.date >= :fromDate) 
                AND (:toDate IS NULL OR tr.date <= :toDate) 
                AND (:traineeName IS NULL OR 
                    LOWER(tr.trainee.lastName) LIKE LOWER(CONCAT('%', :traineeName, '%')) 
                    OR LOWER(tr.trainee.firstName) LIKE LOWER(CONCAT('%', :traineeName, '%')))
            """;

    @Override
    public List<Training> findByTraineeUsername(String username) {
        log.debug("Fetching trainings for trainee '{}'", username);
        String hql = "SELECT tr FROM Training tr WHERE tr.trainee.username = :username";
        List<Training> trainings = entityManager.createQuery(hql, Training.class).setParameter("username", username).getResultList();
        log.info("Found {} trainings for trainee '{}'", trainings.size(), username);
        return trainings;
    }

    @Override
    public List<Training> findByTrainerUsername(String username) {
        log.debug("Fetching trainings for trainer '{}'", username);
        String hql = "SELECT tr FROM Training tr WHERE tr.trainer.username = :username";
        List<Training> trainings = entityManager.createQuery(hql, Training.class).setParameter("username", username).getResultList();
        log.info("Found {} trainings for trainer '{}'", trainings.size(), username);
        return trainings;
    }

    @Override
    public List<Training> findTrainingsForTrainee(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        log.debug("Fetching trainings for trainee '{}' with filters: fromDate={}, toDate={}, trainerName='{}', trainingType='{}'", traineeUsername, fromDate, toDate, trainerName, trainingType);

        TypedQuery<Training> query = entityManager.createQuery(FIND_BY_TRAINEE_CRITERIA, Training.class);
        query.setParameter("traineeUsername", traineeUsername);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("trainerName", trainerName);
        query.setParameter("trainingType", trainingType);

        List<Training> trainings = query.getResultList();
        log.info("Found {} trainings for trainee '{}'", trainings.size(), traineeUsername);
        return trainings;
    }

    @Override
    public List<Training> findTrainingsForTrainer(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        log.debug("Fetching trainings for trainer '{}' with filters: fromDate={}, toDate={}, traineeName='{}'", trainerUsername, fromDate, toDate, traineeName);

        TypedQuery<Training> query = entityManager.createQuery(FIND_BY_TRAINER_CRITERIA, Training.class);
        query.setParameter("trainerUsername", trainerUsername);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("traineeName", traineeName);

        List<Training> trainings = query.getResultList();
        log.info("Found {} trainings for trainer '{}'", trainings.size(), trainerUsername);
        return trainings;
    }

    @Override
    public Training create(Training training) {
        log.info("Creating new training: {}", training);
        entityManager.persist(training);
        log.debug("Training created successfully with ID '{}'", training.getId());
        return training;
    }
}
