package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @InjectMocks
    private TrainerRepositoryImpl trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("testTrainer");
    }

    @Test
    void countByUsernamePrefix_ShouldReturnCorrectCount() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(3L);

        int count = trainerRepository.countByUsernamePrefix("test");

        assertEquals(3, count);
    }

    @Test
    void findByUsername_ShouldReturnTrainer_WhenTrainerExists() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), any())).thenReturn(trainerQuery);
        when(trainerQuery.getSingleResult()).thenReturn(trainer);

        Optional<Trainer> result = trainerRepository.findByUsername("testTrainer");

        assertTrue(result.isPresent());
        assertEquals("testTrainer", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenTrainerDoesNotExist() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), any())).thenReturn(trainerQuery);
        when(trainerQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Trainer> result = trainerRepository.findByUsername("unknownTrainer");

        assertFalse(result.isPresent());
    }

    @Test
    void save_ShouldPersistNewTrainer_WhenIdIsNull() {
        trainer.setId(null);
        doNothing().when(entityManager).persist(trainer);

        Trainer result = trainerRepository.save(trainer);

        assertNull(result.getId());
        verify(entityManager, times(1)).persist(trainer);
    }

    @Test
    void save_ShouldMergeExistingTrainer_WhenIdIsNotNull() {
        doReturn(trainer).when(entityManager).merge(trainer);

        Trainer result = trainerRepository.save(trainer);

        assertEquals(trainer.getId(), result.getId());
        verify(entityManager, times(1)).merge(trainer);
    }
}