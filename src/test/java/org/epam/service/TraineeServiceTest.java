package org.epam.service;

import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepositoryImpl traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUsername("trainee1");
        trainee.setPassword("securePass");

        trainer = new Trainer();
        trainer.setUsername("trainer1");
    }

    @Test
    void testCreate() {
        when(traineeRepository.existsByUsername(anyString())).thenReturn(false);
        when(traineeRepository.create(any(Trainee.class))).thenReturn(trainee);

        traineeService.create(trainee);

        assertNotNull(trainee.getUsername());
        assertNotNull(trainee.getPassword());
        verify(traineeRepository).create(trainee);
    }

    @Test
    void testUpdate() {
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(traineeRepository.update(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.update(trainee);

        assertEquals(trainee, updatedTrainee);
        verify(traineeRepository).update(trainee);
    }

    @Test
    void testFindByUsername() {
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        Trainee found = traineeService.findByUsername("trainee1", "securePass", "trainee1");

        assertEquals(trainee, found);
        verify(traineeRepository, times(2)).findByUsername("trainee1");
    }

    @Test
    void testGetNotMineTrainersByUsername() {
        List<Trainer> trainers = List.of(trainer);

        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(traineeRepository.getNotMineTrainersByUsername("trainee1")).thenReturn(trainers);

        List<Trainer> result = traineeService.getNotMineTrainersByUsername("trainee1", "securePass");

        assertEquals(1, result.size());
        assertEquals(trainer, result.get(0));
        verify(traineeRepository, times(1)).findByUsername("trainee1");
        verify(traineeRepository).getNotMineTrainersByUsername("trainee1");
    }

    @Test
    void testUpdateTrainersList() {
        List<Trainer> trainers = List.of(trainer);
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(traineeRepository.updateTrainersList(trainee, trainers)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainersList("trainee1", "securePass", trainers);

        assertEquals(trainee, updatedTrainee);
        verify(traineeRepository).updateTrainersList(trainee, trainers);
    }

    @Test
    void testChangePassword() {
        String newPassword = "newSecurePass";

        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));
        when(traineeRepository.changePassword(trainee, newPassword)).thenReturn(trainee);

        try (MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.passwordFormatValidator(newPassword)).thenReturn(true);

            Trainee updatedTrainee = traineeService.changePassword(trainee, newPassword);

            assertEquals(trainee, updatedTrainee);
            verify(traineeRepository).changePassword(trainee, newPassword);
        }
    }

    @Test
    void testChangePassword_InvalidFormat() {
        String invalidPassword = "short";

        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        try (MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.passwordFormatValidator(invalidPassword)).thenReturn(false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    traineeService.changePassword(trainee, invalidPassword));

            assertEquals("New Password should be at least 10 chars long", exception.getMessage());
            verify(traineeRepository, never()).changePassword(any(), any());
        }
    }

    @Test
    void testSwitchActivate() {
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        traineeService.switchActivate(trainee);

        verify(traineeRepository).switchActivate("trainee1");
    }

    @Test
    void testDeleteTraineeByUsername() {
        when(traineeRepository.findByUsername("trainee1")).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeByUsername("trainee1", "securePass");

        verify(traineeRepository).deleteByUsername("trainee1");
    }
}