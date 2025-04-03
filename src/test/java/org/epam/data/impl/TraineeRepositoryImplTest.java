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
    void findByUsername_ShouldReturnTrainee_WhenTraineeExists() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "testUser")).thenReturn(traineeQuery);
        when(traineeQuery.getSingleResult()).thenReturn(trainee);

        // Act
        Optional<Trainee> result = traineeRepository.findByUsername("testUser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenTraineeDoesNotExist() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "unknownUser")).thenReturn(traineeQuery);
        when(traineeQuery.getSingleResult()).thenThrow(new NoResultException());

        // Act
        Optional<Trainee> result = traineeRepository.findByUsername("unknownUser");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void getNotMineTrainersByUsername_ShouldReturnTrainers() {
        // Arrange
        List<Trainer> mockTrainers = List.of(new Trainer(), new Trainer());
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", "testUser")).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(mockTrainers);

        // Act
        List<Trainer> result = traineeRepository.getNotMineTrainersByUsername("testUser");

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void save_ShouldPersistNewTrainee_WhenIdIsNull() {
        // Arrange
        trainee.setId(null);
        doNothing().when(entityManager).persist(trainee);

        // Act
        Trainee result = traineeRepository.save(trainee);

        // Assert
        assertNull(result.getId());
        verify(entityManager).persist(trainee);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void save_ShouldMergeExistingTrainee_WhenIdIsNotNull() {
        // Arrange
        when(entityManager.merge(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeRepository.save(trainee);

        // Assert
        assertEquals(trainee.getId(), result.getId());
        verify(entityManager).merge(trainee);
        verify(entityManager, never()).persist(any());
    }

    @Test
    void delete_ShouldRemoveTrainee_WhenExists() {
        // Arrange
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);
        doNothing().when(entityManager).remove(trainee);

        // Act
        traineeRepository.delete(trainee);

        // Assert
        verify(entityManager).remove(trainee);
    }

    @Test
    void delete_ShouldDoNothing_WhenTraineeDoesNotExist() {
        // Arrange
        when(entityManager.find(Trainee.class, 1L)).thenReturn(null);

        // Act
        traineeRepository.delete(trainee);

        // Assert
        verify(entityManager, never()).remove(any());
    }
}