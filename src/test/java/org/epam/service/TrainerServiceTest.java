package org.epam.service;

import org.epam.data.TrainerDao;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
    }

    @Test
    void save_ShouldGenerateUsernameAndPasswordAndSaveTrainer() {
        when(trainerDao.existsByUsername(anyString())).thenReturn(false);

        trainerService.create(trainer);

        assertNotNull(trainer.getUsername());
        assertNotNull(trainer.getPassword());
        verify(trainerDao).save(trainer);
    }

    @Test
    void findById_ShouldReturnTrainer_WhenExists() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.findById(1L);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer, foundTrainer.get());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTrainerDoesNotExist() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        Optional<Trainer> foundTrainer = trainerService.findById(1L);

        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void findAll_ShouldReturnListOfTrainers() {
        when(trainerDao.findAll()).thenReturn(Arrays.asList(trainer));

        assertEquals(1, trainerService.findAll().size());
    }
}
