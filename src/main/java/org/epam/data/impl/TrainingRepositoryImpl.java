package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.epam.data.TrainingRepository;
import org.epam.model.Training;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

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


    public List<Training> findTrainingsByCriteria(String username, Date fromDate, Date toDate, String nameFilter, String userType, String trainingType) {
        log.debug("Fetching trainings for {} '{}' with filters: fromDate={}, toDate={}, nameFilter='{}', trainingType='{}'", userType, username, fromDate, toDate, nameFilter, trainingType);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> trainingRoot = cq.from(Training.class);

        Join<?, ?> userJoin = null;
        if ("trainer".equals(userType)) {
            userJoin = trainingRoot.join("trainer");
        } else if ("trainee".equals(userType)) {
            userJoin = trainingRoot.join("trainee");
        }

        Join<?, ?> trainingTypeJoin = trainingRoot.join("type", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (userJoin != null) {
            predicates.add(cb.equal(userJoin.get("username"), username));
        }

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("date"), toDate));
        }

        if (nameFilter != null && !nameFilter.isBlank()) {
            Predicate lastNameLike = cb.like(cb.lower(userJoin.get("lastName")), "%" + nameFilter + "%");
            Predicate firstNameLike = cb.like(cb.lower(userJoin.get("firstName")), "%" + nameFilter + "%");
            predicates.add(cb.or(lastNameLike, firstNameLike));
        }

        if (trainingType != null && !trainingType.isBlank()) {
            predicates.add(cb.equal(trainingTypeJoin.get("trainingTypeName"), trainingType));
        }

        cq.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Training> query = entityManager.createQuery(cq);

        List<Training> trainings = query.getResultList();
        log.info("Found {} trainings for {} '{}'", trainings.size(), userType, username);
        return trainings;
    }

    @Override
    public Training create(Training training) {
        log.info("Creating new training: {}", training);
        entityManager.persist(training);
        log.debug("Training created successfully with ID '{}'", training.getId());
        return training;
    }

    public Optional<Training> findTrainingById(Integer id) {
        log.debug("Fetching training by id '{}'", id);
        var training = entityManager.find(Training.class, id);
        if (training == null) {
            return Optional.empty();
        } else
            return Optional.of(training);
    }

    public void delete(Training training) {
        log.info("Deleting training with id '{}'", training.getId());

        var existingTraining = entityManager.find(Training.class, training.getId());
        if (existingTraining != null) {
            entityManager.remove(existingTraining);
            log.info("training '{}' deleted successfully", training.getId());
        } else {
            log.warn("No training found with id '{}' to delete", training.getId());
        }
    }
}
