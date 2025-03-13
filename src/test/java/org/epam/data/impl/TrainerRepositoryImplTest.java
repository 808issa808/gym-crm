package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @Mock
    private Query updateQuery;

    @InjectMocks
    private TrainerRepositoryImpl trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setUsername("testTrainer");
        trainer.setPassword("password");
        trainer.setActive(true);

        lenient().when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        lenient().when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        lenient().when(entityManager.createQuery(anyString())).thenReturn(updateQuery);
    }


    @Test
    void testExistsByUsername_TrainerExists() {
        when(countQuery.setParameter("username", "testTrainer")).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean exists = trainerRepository.existsByUsername("testTrainer");

        assertTrue(exists);
    }

    @Test
    void testExistsByUsername_TrainerNotExists() {
        when(countQuery.setParameter("username", "testTrainer")).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        boolean exists = trainerRepository.existsByUsername("testTrainer");

        assertFalse(exists);
    }

    @Test
    void testFindByUsername_TrainerExists() {
        when(trainerQuery.setParameter("username", "testTrainer")).thenReturn(trainerQuery);
        when(trainerQuery.getResultStream()).thenReturn(Stream.of(trainer));

        Optional<Trainer> result = trainerRepository.findByUsername("testTrainer");

        assertTrue(result.isPresent());
        assertEquals("testTrainer", result.get().getUsername());
    }

    @Test
    void testFindByUsername_TrainerNotExists() {
        when(trainerQuery.setParameter("username", "testTrainer")).thenReturn(trainerQuery);
        when(trainerQuery.getResultStream()).thenReturn(Stream.empty());

        Optional<Trainer> result = trainerRepository.findByUsername("testTrainer");

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateTrainer() {
        doNothing().when(entityManager).persist(any(Trainer.class));

        Trainer createdTrainer = trainerRepository.create(trainer);

        assertNotNull(createdTrainer);
        assertEquals("testTrainer", createdTrainer.getUsername());
        verify(entityManager, times(1)).persist(trainer);
    }

    @Test
    void testChangePassword() {
        when(entityManager.merge(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerRepository.changePassword(trainer, "newPassword");

        assertNotNull(updatedTrainer);
        assertEquals("newPassword", updatedTrainer.getPassword());
        verify(entityManager, times(1)).merge(trainer);
    }

    @Test
    void testUpdateTrainer() {
        when(entityManager.merge(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerRepository.update(trainer);

        assertNotNull(updatedTrainer);
        assertEquals("testTrainer", updatedTrainer.getUsername());
        verify(entityManager, times(1)).merge(trainer);
    }

    @Test
    void testSwitchActivate_TrainerExists() {
        when(updateQuery.setParameter("username", "testTrainer")).thenReturn(updateQuery);
        when(updateQuery.executeUpdate()).thenReturn(1);

        trainerRepository.switchActivate("testTrainer");

        verify(entityManager, times(1)).createQuery(anyString());
        verify(updateQuery, times(1)).executeUpdate();
    }

    @Test
    void testSwitchActivate_TrainerNotExists() {
        when(updateQuery.setParameter("username", "testTrainer")).thenReturn(updateQuery);
        when(updateQuery.executeUpdate()).thenReturn(0);

        trainerRepository.switchActivate("testTrainer");

        verify(entityManager, times(1)).createQuery(anyString());
        verify(updateQuery, times(1)).executeUpdate();
    }
}