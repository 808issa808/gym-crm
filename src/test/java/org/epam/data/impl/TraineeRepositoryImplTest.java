package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> longQuery;

    @Mock
    private TypedQuery<Trainee> traineeQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername("testUser");
    }

    @Test
    void countByUsernamePrefix_ShouldReturnCorrectCount() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), anyString())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(5L);

        int count = traineeRepository.countByUsernamePrefix("test");

        assertEquals(5, count);
    }

    @Test
    void findByUsername_ShouldReturnTrainee_WhenTraineeExists() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter(anyString(), any())).thenReturn(traineeQuery);
        when(traineeQuery.getSingleResult()).thenReturn(trainee);

        Optional<Trainee> result = traineeRepository.findByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenTraineeDoesNotExist() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter(anyString(), any())).thenReturn(traineeQuery);
        when(traineeQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Trainee> result = traineeRepository.findByUsername("unknownUser");

        assertFalse(result.isPresent());
    }

    @Test
    void getNotMineTrainersByUsername_ShouldReturnTrainers() {
        List<Trainer> mockTrainers = List.of(new Trainer(), new Trainer());

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), any())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(mockTrainers);

        List<Trainer> trainers = traineeRepository.getNotMineTrainersByUsername("testUser");

        assertEquals(2, trainers.size());
    }

    @Test
    void save_ShouldPersistNewTrainee_WhenIdIsNull() {
        trainee.setId(null);
        doNothing().when(entityManager).persist(trainee);

        Trainee result = traineeRepository.save(trainee);

        assertNull(result.getId());
        verify(entityManager, times(1)).persist(trainee);
    }

    @Test
    void save_ShouldMergeExistingTrainee_WhenIdIsNotNull() {
        doReturn(trainee).when(entityManager).merge(trainee);

        Trainee result = traineeRepository.save(trainee);

        assertEquals(trainee.getId(), result.getId());
        verify(entityManager, times(1)).merge(trainee);
    }

    @Test
    void delete_ShouldRemoveTrainee_WhenExists() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(trainee);
        doNothing().when(entityManager).remove(trainee);

        traineeRepository.delete(trainee);

        verify(entityManager, times(1)).remove(trainee);
    }

    @Test
    void delete_ShouldDoNothing_WhenTraineeDoesNotExist() {
        when(entityManager.find(Trainee.class, trainee.getId())).thenReturn(null);

        traineeRepository.delete(trainee);

        verify(entityManager, never()).remove(any());
    }
}