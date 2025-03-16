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


    @Override
    public List<Training> findTrainingsForTrainer(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        log.debug("Fetching trainings for trainer '{}' with filters: fromDate={}, toDate={}, traineeName='{}'", trainerUsername, fromDate, toDate, traineeName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> trainingRoot = cq.from(Training.class);

        // Присоединяем нужные сущности
        Join<?, ?> trainerJoin = trainingRoot.join("trainer");
        Join<?, ?> traineeJoin = trainingRoot.join("trainee", JoinType.LEFT);

        // Список условий
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(trainerJoin.get("username"), trainerUsername));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("date"), toDate));
        }
        if (traineeName != null && !traineeName.isBlank()) {
            Predicate trainerLastNameLike = cb.like(cb.lower(traineeJoin.get("lastName")), "%" + traineeName + "%");
            Predicate trainerFirstNameLike = cb.like(cb.lower(traineeJoin.get("firstName")), "%" + traineeName + "%");
            predicates.add(cb.or(trainerLastNameLike, trainerFirstNameLike));
        }

        // Собираем запрос
        cq.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Training> query = entityManager.createQuery(cq);

        List<Training> trainings = query.getResultList();
        log.info("Found {} trainings for trainer '{}'", trainings.size(), trainerUsername);
        return trainings;
    }

    @Override
    public List<Training> findTrainingsForTraineeByCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        log.debug("Fetching trainings for trainee '{}' with filters: fromDate={}, toDate={}, trainerName='{}', trainingType='{}'", traineeUsername, fromDate, toDate, trainerName, trainingType);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> trainingRoot = cq.from(Training.class);

        // Присоединяем нужные сущности
        Join<?, ?> traineeJoin = trainingRoot.join("trainee");
        Join<?, ?> trainerJoin = trainingRoot.join("trainer", JoinType.LEFT);
        Join<?, ?> trainingTypeJoin = trainingRoot.join("type", JoinType.LEFT);

        // Список условий
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(traineeJoin.get("username"), traineeUsername));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(trainingRoot.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(trainingRoot.get("date"), toDate));
        }
        if (trainerName != null && !trainerName.isBlank()) {
            Predicate trainerLastNameLike = cb.like(cb.lower(trainerJoin.get("lastName")), "%" + trainerName + "%");
            Predicate trainerFirstNameLike = cb.like(cb.lower(trainerJoin.get("firstName")), "%" + trainerName + "%");
            predicates.add(cb.or(trainerLastNameLike, trainerFirstNameLike));
        }
        if (trainingType != null && !trainingType.isBlank()) {
            predicates.add(cb.equal(trainingTypeJoin.get("trainingTypeName"), trainingType));
        }

        // Собираем запрос
        cq.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Training> query = entityManager.createQuery(cq);

        List<Training> trainings = query.getResultList();
        log.info("Found {} trainings for trainee '{}'", trainings.size(), traineeUsername);
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
