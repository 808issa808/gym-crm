package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.epam.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Training> typedQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Training> criteriaQuery;

    @Mock
    private Root<Training> root;

    @Mock
    private Join<Object, Object> traineeJoin;

    @Mock
    private Join<Object, Object> trainerJoin;

    @Mock
    private Join<Object, Object> trainingTypeJoin;

    @InjectMocks
    private TrainingRepositoryImpl trainingRepository;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(1L);
    }

    @Test
    void findByTraineeUsername_ShouldReturnTrainings() {
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        List<Training> result = trainingRepository.findByTraineeUsername("testUser");

        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
    }

    @Test
    void findByTrainerUsername_ShouldReturnTrainings() {
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        List<Training> result = trainingRepository.findByTrainerUsername("testTrainer");

        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
    }

    @Test
    void findTrainingsForTrainer_ShouldReturnTrainings() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);

        // Мокируем присоединения
        when(root.join("trainer")).thenReturn(trainerJoin);
        when(root.join("trainee", JoinType.LEFT)).thenReturn(traineeJoin);

        // Мокируем select и where, чтобы избежать NPE
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        List<Training> result = trainingRepository.findTrainingsForTrainer("trainer1", new Date(), new Date(), "John");

        assertEquals(1, result.size());
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    void findTrainingsForTraineeByCriteria_ShouldReturnTrainings() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);

        // Мокируем присоединения
        when(root.join("trainee")).thenReturn(traineeJoin);
        when(root.join("trainer", JoinType.LEFT)).thenReturn(trainerJoin);
        when(root.join("type", JoinType.LEFT)).thenReturn(trainingTypeJoin);

        // Мокируем select и where, чтобы избежать NPE
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        List<Training> result = trainingRepository.findTrainingsForTraineeByCriteria("trainee1", new Date(), new Date(), "Doe", "Yoga");

        assertEquals(1, result.size());
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    void create_ShouldPersistTraining() {
        doNothing().when(entityManager).persist(training);

        Training result = trainingRepository.create(training);

        assertEquals(training, result);
        verify(entityManager).persist(training);
    }
}