package org.epam.service;

import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.doe");
        trainer.setPassword("securePass123");
        trainer.setActive(true);
    }

    @Test
    void create_ShouldGenerateUsernameAndSaveTrainer() {
        when(trainerRepository.countByUsernamePrefix(any())).thenReturn(0);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer savedTrainer = trainerService.create(trainer);

        assertNotNull(savedTrainer.getUsername());
        assertNotNull(savedTrainer.getPassword());
        assertTrue(savedTrainer.isActive());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void findByUsername_ShouldReturnTrainer_WhenExists() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

        Trainer foundTrainer = trainerService.findByUsername("john.doe", "securePass123", "john.doe");

        assertNotNull(foundTrainer);
        assertEquals("john.doe", foundTrainer.getUsername());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenTrainerNotFound() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer)); // Убеждаемся, что аутентификация пройдет

        when(trainerRepository.findByUsername("unknown")).thenReturn(Optional.empty()); // А вот здесь нет пользователя

        Exception exception = assertThrows(IllegalArgumentException.class, () -> trainerService.findByUsername("john.doe", "securePass123", "unknown"));

        assertEquals("There is no trainer with username: unknown", exception.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenValid() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.changePassword(trainer, "newSecurePass123");

        assertEquals("newSecurePass123", updatedTrainer.getPassword());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void changePassword_ShouldThrowException_WhenPasswordTooShort() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(trainer, "short"));

        assertEquals("New Password should be at least 10 chars long", exception.getMessage());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void update_ShouldSaveTrainer() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.update(trainer);

        assertNotNull(updatedTrainer);
        verify(trainerRepository).save(trainer);
    }

    @Test
    void switchActivate_ShouldToggleActiveState() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        trainerService.switchActivate(trainer);

        assertFalse(trainer.isActive());
        verify(trainerRepository).save(trainer);
    }
}