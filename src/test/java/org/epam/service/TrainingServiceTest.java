package org.epam.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Training;
import org.epam.model.User;
import org.epam.util.Authenticator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;;

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

    private MockedStatic<Authenticator> mockedAuthenticator;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(1L);
        mockedAuthenticator = mockStatic(Authenticator.class);
    }

    @AfterEach
    void tearDown() {
        mockedAuthenticator.close();
    }

    @Test
    void testCreate() {
        trainingService.create(training);
        verify(trainingRepository).create(training);
    }

}