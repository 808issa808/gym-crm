package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.epam.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Training> typedQuery;

    @InjectMocks
    private TrainingRepositoryImpl trainingRepository;

    private Training training1;
    private Training training2;

    @BeforeEach
    void setUp() {
        training1 = new Training();
        training1.setId(1L);

        training2 = new Training();
        training2.setId(2L);
    }

    @Test
    void testFindByTraineeUsername() {
        String username = "trainee1";
        List<Training> expectedTrainings = Arrays.asList(training1, training2);

        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTrainings);

        List<Training> result = trainingRepository.findByTraineeUsername(username);

        assertEquals(2, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
        verify(typedQuery).setParameter("username", username);
        verify(typedQuery).getResultList();
    }

    @Test
    void testFindByTrainerUsername() {
        String username = "trainer1";
        List<Training> expectedTrainings = Arrays.asList(training1);

        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTrainings);

        List<Training> result = trainingRepository.findByTrainerUsername(username);

        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
        verify(typedQuery).setParameter("username", username);
        verify(typedQuery).getResultList();
    }

    @Test
    void testFindTrainingsForTrainee() {
        String traineeUsername = "trainee1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "John";
        String trainingType = "Cardio";

        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(training1, training2));

        List<Training> result = trainingRepository.findTrainingsForTrainee(traineeUsername, fromDate, toDate, trainerName, trainingType);

        assertEquals(2, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
        verify(typedQuery, atLeastOnce()).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @Test
    void testFindTrainingsForTrainer() {
        String trainerUsername = "trainer1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Alice";

        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(training1));

        List<Training> result = trainingRepository.findTrainingsForTrainer(trainerUsername, fromDate, toDate, traineeName);

        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Training.class));
        verify(typedQuery, atLeastOnce()).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @Test
    void testCreate() {
        doNothing().when(entityManager).persist(training1);

        Training result = trainingRepository.create(training1);

        assertEquals(training1, result);
        verify(entityManager).persist(training1);
    }
}