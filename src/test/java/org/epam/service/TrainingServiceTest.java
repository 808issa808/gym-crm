package org.epam.service;

import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.util.Authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepositoryImpl trainingRepository;

    @Mock
    private TraineeRepositoryImpl traineeRepository;

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(1L);
    }

    @Test
    void shouldCreateTraining() {
        trainingService.create(training);
        verify(trainingRepository, times(1)).create(training);
    }

    @Test
    void shouldFindByTraineeUsername() {
        String username = "trainee1";
        String password = "password";
        List<Training> expectedTrainings = Collections.singletonList(training);

        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            // Правильный способ замока void-метода
            mockedAuthenticator.when(() -> Authenticator.authenticateUser(username, password, traineeRepository::findByUsername)).thenAnswer(invocation -> null); // Просто ничего не делать

            // Замокали `findByTraineeUsername`
            when(trainingRepository.findByTraineeUsername(username)).thenReturn(expectedTrainings);

            List<Training> result = trainingService.findByTraineeUsername(username, password);

            assertEquals(expectedTrainings, result);
            verify(trainingRepository, times(1)).findByTraineeUsername(username);
        }
    }


    @Test
    void shouldFindByTrainerUsername() {
        String username = "trainer1";
        String password = "password";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(password);
        List<Training> expectedTrainings = Collections.singletonList(training);

        // Замокайте правильный ответ для findByUsername
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));

        // Замокайте ответ для findByTrainerUsername
        when(trainingRepository.findByTrainerUsername(username)).thenReturn(expectedTrainings);

        List<Training> result = trainingService.findByTrainerUsername(username, password);

        assertEquals(expectedTrainings, result);
        verify(trainingRepository, times(1)).findByTrainerUsername(username);
    }

    @Test
    void shouldFindTrainingsForTrainee() {
        String username = "trainee1";
        String password = "password";
        String traineeUsername = "trainee1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer1";
        String trainingType = "yoga";

        List<Training> expectedTrainings = Collections.singletonList(training);

        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            // Мокаем вызов аутентификации (void-метод)
            mockedAuthenticator.when(() -> Authenticator.authenticateUser(username, password, traineeRepository::findByUsername)).thenAnswer(invocation -> null); // Просто ничего не делать

            // Мокаем вызов репозитория
            when(trainingRepository.findTrainingsForTraineeByCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType)).thenReturn(expectedTrainings);

            List<Training> result = trainingService.findTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType);

            assertEquals(expectedTrainings, result);
            verify(trainingRepository, times(1)).findTrainingsForTraineeByCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
        }
    }

    @Test
    void shouldFindTrainingsForTrainer() {
        String username = "trainer1";
        String password = "password";
        String trainerUsername = "trainer1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee1";

        List<Training> expectedTrainings = Collections.singletonList(training);

        try (MockedStatic<Authenticator> mockedAuthenticator = mockStatic(Authenticator.class)) {
            // Мокаем статический метод аутентификации
            mockedAuthenticator.when(() -> Authenticator.authenticateUser(username, password, trainerRepository::findByUsername)).thenAnswer(invocation -> null); // Ничего не делать

            // Мокаем метод репозитория
            when(trainingRepository.findTrainingsForTrainerByCriteria(trainerUsername, fromDate, toDate, traineeName)).thenReturn(expectedTrainings);

            List<Training> result = trainingService.findTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, traineeName);

            assertEquals(expectedTrainings, result);
            verify(trainingRepository, times(1)).findTrainingsForTrainerByCriteria(trainerUsername, fromDate, toDate, traineeName);
        }
    }

}