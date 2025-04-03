package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.model.TrainingType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TrainingTypeRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public Optional<TrainingType> findById(Long id) {
        log.debug("Fetching TrainingType by id '{}'", id);
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    public List<TrainingType> findAll() {
        log.debug("Fetching all TrainingTypes");
        return entityManager.createQuery("SELECT t FROM TrainingType t", TrainingType.class)
                .getResultList();
    }
}