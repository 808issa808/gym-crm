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
    private Join<Object, Object> userJoin;

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
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", "testUser")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        // Act
        List<Training> result = trainingRepository.findByTraineeUsername("testUser");

        // Assert
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
    }

    @Test
    void findByTrainerUsername_ShouldReturnTrainings() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", "testTrainer")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        // Act
        List<Training> result = trainingRepository.findByTrainerUsername("testTrainer");

        // Assert
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
    }

    @Test
    void findTrainingsByCriteria_ShouldReturnTrainingsForTrainer() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(root.join("trainer")).thenReturn(userJoin);
        when(root.join("type", JoinType.LEFT)).thenReturn(trainingTypeJoin);

        // Mock criteria query building
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        Date fromDate = new Date();
        Date toDate = new Date();

        // Act
        List<Training> result = trainingRepository.findTrainingsByCriteria(
                "trainer1", fromDate, toDate, "John", "trainer", "Yoga");

        // Assert
        assertEquals(1, result.size());
        verify(root).join("trainer");
        verify(root).join("type", JoinType.LEFT);
    }

    @Test
    void findTrainingsByCriteria_ShouldReturnTrainingsForTrainee() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(root.join("trainee")).thenReturn(userJoin);
        when(root.join("type", JoinType.LEFT)).thenReturn(trainingTypeJoin);

        // Mock criteria query building
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        Date fromDate = new Date();
        Date toDate = new Date();

        // Act
        List<Training> result = trainingRepository.findTrainingsByCriteria(
                "trainee1", fromDate, toDate, "Doe", "trainee", "Yoga");

        // Assert
        assertEquals(1, result.size());
        verify(root).join("trainee");
        verify(root).join("type", JoinType.LEFT);
    }

    @Test
    void findTrainingsByCriteria_ShouldHandleNullFilters() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(root.join("trainee")).thenReturn(userJoin);
        when(root.join("type", JoinType.LEFT)).thenReturn(trainingTypeJoin);

        // Mock criteria query building
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(training));

        // Act
        List<Training> result = trainingRepository.findTrainingsByCriteria(
                "trainee1", null, null, null, "trainee", null);

        // Assert
        assertEquals(1, result.size());
        verify(criteriaQuery).where(any(Predicate[].class));
    }

    @Test
    void create_ShouldPersistTraining() {
        // Arrange
        doNothing().when(entityManager).persist(training);

        // Act
        Training result = trainingRepository.create(training);

        // Assert
        assertEquals(training, result);
        verify(entityManager).persist(training);
    }
}