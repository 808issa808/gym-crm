package org.epam.service;

import org.epam.data.impl.TraineeRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepositoryImpl traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
    }

    @Test
    void create_ShouldGenerateUsernameAndSaveTrainee() {
        when(traineeRepository.countByUsernamePrefix(any())).thenReturn(0);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.create(trainee);

        assertNotNull(trainee.getUsername());
        assertNotNull(trainee.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void update_ShouldAuthenticateAndUpdateTrainee() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.update(trainee);

        assertEquals(trainee, updatedTrainee);
        verify(traineeRepository).save(trainee);
    }

    @Test
    void findByUsername_ShouldReturnTrainee_WhenExists() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        Trainee foundTrainee = traineeService.findByUsername("john.doe", "password123", "john.doe");

        assertEquals(trainee, foundTrainee);
    }

    @Test
    void findByUsername_ShouldThrowException_WhenTraineeNotFound() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> traineeService.findByUsername("john.doe", "password123", "john.doe"));
    }

    @Test
    void getNotMineTrainersByUsername_ShouldReturnTrainerList() {
        List<Trainer> trainers = List.of(new Trainer());
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.getNotMineTrainersByUsername("john.doe")).thenReturn(trainers);

        List<Trainer> result = traineeService.getNotMineTrainersByUsername("john.doe", "password123");

        assertEquals(trainers, result);
    }

    @Test
    void updateTrainersList_ShouldUpdateTrainers() {
        List<Trainer> trainers = List.of(new Trainer());
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainersList("john.doe", "password123", trainers);

        assertEquals(trainers, updatedTrainee.getTrainers());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenValid() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.changePassword(trainee, "newSecurePassword");

        assertEquals("newSecurePassword", updatedTrainee.getPassword());
    }

    @Test
    void changePassword_ShouldThrowException_WhenPasswordTooShort() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(trainee, "short"));
    }

    @Test
    void switchActivate_ShouldToggleActiveState() {
        trainee.setActive(true);
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.switchActivate(trainee);

        assertFalse(trainee.isActive());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void deleteTraineeByUsername_ShouldDeleteTrainee() {
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(any(Trainee.class));

        traineeService.deleteTraineeByUsername("john.doe", "password123");

        verify(traineeRepository).delete(trainee);
    }
}