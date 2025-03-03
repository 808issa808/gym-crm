package org.epam.facade;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
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
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    private Trainee testTrainee;
    private Trainer testTrainer;
    private Training testTraining;

    @BeforeEach
    void setUp() {
        testTrainee = new Trainee();
        testTrainee.setUserId(1L);
        testTrainee.setFirstName("John");
        testTrainee.setLastName("Doe");

        testTrainer = new Trainer();
        testTrainer.setUserId(1L);
        testTrainer.setFirstName("Jane");
        testTrainer.setLastName("Smith");

        testTraining = new Training();
        testTraining.setName("Yoga Class");
    }

    // --- Тесты для Trainee ---

    @Test
    void createTrainee_ShouldCallServiceMethod() {
        gymFacade.createTrainee(testTrainee);
        verify(traineeService, times(1)).create(testTrainee);
    }

    @Test
    void updateTrainee_ShouldReturnUpdatedTrainee() {
        when(traineeService.update(testTrainee)).thenReturn(testTrainee);

        Trainee result = gymFacade.updateTrainee(testTrainee);

        assertEquals(testTrainee, result);
        verify(traineeService, times(1)).update(testTrainee);
    }

    @Test
    void findTraineeById_ShouldReturnTrainee() {
        when(traineeService.findById(1L)).thenReturn(Optional.of(testTrainee));

        Optional<Trainee> result = gymFacade.findTraineeById(1L);

        assertTrue(result.isPresent());
        assertEquals(testTrainee, result.get());
        verify(traineeService, times(1)).findById(1L);
    }

    @Test
    void findAllTrainees_ShouldReturnListOfTrainees() {
        when(traineeService.findAll()).thenReturn(List.of(testTrainee));

        List<Trainee> result = (List<Trainee>) gymFacade.findAllTrainees();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(traineeService, times(1)).findAll();
    }

    @Test
    void deleteTraineeById_ShouldCallServiceMethod() {
        gymFacade.deleteTraineeById(1L);
        verify(traineeService, times(1)).deleteById(1L);
    }

    // --- Тесты для Trainer ---

    @Test
    void createTrainer_ShouldCallServiceMethod() {
        gymFacade.createTrainer(testTrainer);
        verify(trainerService, times(1)).create(testTrainer);
    }

    @Test
    void findTrainerById_ShouldReturnTrainer() {
        when(trainerService.findById(1L)).thenReturn(Optional.of(testTrainer));

        Optional<Trainer> result = gymFacade.findTrainerById(1L);

        assertTrue(result.isPresent());
        assertEquals(testTrainer, result.get());
        verify(trainerService, times(1)).findById(1L);
    }

    @Test
    void findAllTrainers_ShouldReturnListOfTrainers() {
        when(trainerService.findAll()).thenReturn(List.of(testTrainer));

        List<Trainer> result = (List<Trainer>) gymFacade.findAllTrainers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(trainerService, times(1)).findAll();
    }

    // --- Тесты для Training ---

    @Test
    void createTraining_ShouldCallServiceMethod() {
        gymFacade.createTraining(testTraining);
        verify(trainingService, times(1)).create(testTraining);
    }

    @Test
    void findTrainingByTraineeId_ShouldReturnTraining() {
        when(trainingService.findByTraineeId(1L)).thenReturn(Optional.of(testTraining));

        Optional<Training> result = gymFacade.findTrainingByTraineeId(1L);

        assertTrue(result.isPresent());
        assertEquals(testTraining, result.get());
        verify(trainingService, times(1)).findByTraineeId(1L);
    }

    @Test
    void findAllTrainings_ShouldReturnListOfTrainings() {
        when(trainingService.findAll()).thenReturn(List.of(testTraining));

        List<Training> result = (List<Training>) gymFacade.findAllTrainings();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(trainingService, times(1)).findAll();
    }
}
