package org.epam.service;

import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainingCreateDto;
import org.epam.web.dto.users.UserCredentialsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepositoryImpl trainingRepository;

    @Mock
    private TraineeRepositoryImpl traineeRepository;

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;
    private UserCredentialsDto credentials;

    @BeforeEach
    void setUp() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Yoga");

        trainee = new Trainee();
        trainee.setUsername("trainee.user");
        trainee.setPassword("traineePass");

        trainer = new Trainer();
        trainer.setUsername("trainer.user");
        trainer.setPassword("trainerPass");
        trainer.setSpecialization(trainingType);

        training = new Training();
        training.setId(1L);
        training.setName("Morning Yoga");
        training.setDate(new Date());
        training.setDuration(60);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainingType);

        credentials = new UserCredentialsDto("trainee.user", "traineePass");
    }

    @Test
    void create_ShouldSaveTraining_WhenValidData() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto();
        createDto.setAuth(credentials);
        createDto.setTrainee("trainee.user");
        createDto.setTrainer("trainer.user");
        createDto.setName("Morning Yoga");
        createDto.setDate(new Date());
        createDto.setDuration(60);

        when(traineeRepository.findByUsername("trainee.user")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("trainer.user")).thenReturn(Optional.of(trainer));
        when(modelMapper.map(createDto, Training.class)).thenReturn(training);
        when(trainingRepository.create(training)).thenReturn(training);

        // Act
        trainingService.create(createDto);

        // Assert
        verify(trainingRepository).create(training);
        assertEquals(trainer.getSpecialization(), training.getType());
    }

    @Test
    void create_ShouldThrowException_WhenTraineeNotFound() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto();
        createDto.setAuth(credentials);
        createDto.setTrainee("unknown.trainee");
        createDto.setTrainer("trainer.user");

        // Stub the authentication call
        when(traineeRepository.findByUsername("trainee.user")).thenReturn(Optional.of(trainee));
        // Stub the actual test case call
        when(traineeRepository.findByUsername("unknown.trainee")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainingService.create(createDto));
        assertEquals("No trainee with given username : unknown.trainee exists.", exception.getMessage());

        // Verify no interactions with trainer repository
        verifyNoInteractions(trainerRepository);
    }

    @Test
    void create_ShouldThrowException_WhenTrainerNotFound() {
        // Arrange
        TrainingCreateDto createDto = new TrainingCreateDto();
        createDto.setAuth(credentials);
        createDto.setTrainee("trainee.user");
        createDto.setTrainer("unknown.trainer");

        when(traineeRepository.findByUsername("trainee.user")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("unknown.trainer")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainingService.create(createDto));
        assertEquals("There is no trainer with username: unknown.trainer", exception.getMessage());
    }

    @Test
    void findByTraineeUsername_ShouldReturnTrainingsList() {
        // Arrange
        when(traineeRepository.findByUsername("trainee.user")).thenReturn(Optional.of(trainee));
        when(trainingRepository.findByTraineeUsername("trainee.user")).thenReturn(List.of(training));

        // Act
        List<Training> result = trainingService.findByTraineeUsername("trainee.user", "traineePass");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(training, result.get(0));
    }

    @Test
    void findByTrainerUsername_ShouldReturnTrainingsList() {
        // Arrange
        when(trainerRepository.findByUsername("trainer.user")).thenReturn(Optional.of(trainer));
        when(trainingRepository.findByTrainerUsername("trainer.user")).thenReturn(List.of(training));

        // Act
        List<Training> result = trainingService.findByTrainerUsername("trainer.user", "trainerPass");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(training, result.get(0));
    }

    @Test
    void findTrainingsForTrainee_ShouldReturnFilteredTrainings() {
        // Arrange
        GetTraineeTrainingsResponse response = new GetTraineeTrainingsResponse();
        Date fromDate = new Date(System.currentTimeMillis() - 100000);
        Date toDate = new Date();

        when(traineeRepository.findByUsername("trainee.user")).thenReturn(Optional.of(trainee));
        when(trainingRepository.findTrainingsByCriteria(
                "trainee.user", fromDate, toDate, "trainer.user", "trainee", "Yoga"))
                .thenReturn(List.of(training));
        when(modelMapper.map(training, GetTraineeTrainingsResponse.class)).thenReturn(response);

        // Act
        List<GetTraineeTrainingsResponse> result = trainingService.findTrainingsForTrainee(
                "trainee.user", "traineePass", "trainee.user", fromDate, toDate, "trainer.user", "Yoga");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(response, result.get(0));
    }

    @Test
    void findTrainingsForTrainer_ShouldReturnFilteredTrainings() {
        // Arrange
        GetTrainerTrainingsResponse response = new GetTrainerTrainingsResponse();
        Date fromDate = new Date(System.currentTimeMillis() - 100000);
        Date toDate = new Date();

        when(trainerRepository.findByUsername("trainer.user")).thenReturn(Optional.of(trainer));
        when(trainingRepository.findTrainingsByCriteria(
                "trainer.user", fromDate, toDate, "trainee.user", "trainer", null))
                .thenReturn(List.of(training));
        when(modelMapper.map(training, GetTrainerTrainingsResponse.class)).thenReturn(response);

        // Act
        List<GetTrainerTrainingsResponse> result = trainingService.findTrainingsForTrainer(
                "trainer.user", "trainerPass", "trainer.user", fromDate, toDate, "trainee.user");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(response, result.get(0));
    }
}