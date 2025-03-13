package org.epam.facade;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---- Тесты для Trainee ----
    @Test
    void testCreateTrainee() {
        Trainee trainee = new Trainee();
        gymFacade.createTrainee(trainee);
        verify(traineeService, times(1)).create(trainee);
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        when(traineeService.update(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = gymFacade.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        verify(traineeService, times(1)).update(trainee);
    }

    @Test
    void testGetTrainee() {
        String username = "user1";
        String password = "pass";
        String searchedUsername = "searchedUser";
        Trainee trainee = new Trainee();
        when(traineeService.findByUsername(username, password, searchedUsername)).thenReturn(trainee);

        Trainee result = gymFacade.getTrainee(username, password, searchedUsername);

        assertNotNull(result);
        verify(traineeService, times(1)).findByUsername(username, password, searchedUsername);
    }

    @Test
    void testDeleteTrainee() {
        String username = "user1";
        String password = "pass";
        gymFacade.deleteTrainee(username, password);
        verify(traineeService, times(1)).deleteTraineeByUsername(username, password);
    }

    @Test
    void testChangeTraineePassword() {
        Trainee trainee = new Trainee();
        String newPassword = "newPass";

        gymFacade.changeTraineePassword(trainee, newPassword);

        verify(traineeService, times(1)).changePassword(trainee, newPassword);
    }

    @Test
    void testActivateOrDeactivateTrainee() {
        Trainee trainee = new Trainee();

        gymFacade.activateOrDeactivateTrainee(trainee);

        verify(traineeService, times(1)).switchActivate(trainee);
    }

    @Test
    void testGetAvailableTrainersForTrainee() {
        String username = "user1";
        String password = "pass";
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
        when(traineeService.getNotMineTrainersByUsername(username, password)).thenReturn(trainers);

        List<Trainer> result = gymFacade.getAvailableTrainersForTrainee(username, password);

        assertEquals(2, result.size());
        verify(traineeService, times(1)).getNotMineTrainersByUsername(username, password);
    }

    // ---- Тесты для Trainer ----
    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        gymFacade.createTrainer(trainer);
        verify(trainerService, times(1)).create(trainer);
    }

    @Test
    void testGetTrainer() {
        String username = "trainer1";
        String password = "pass";
        String searchedUsername = "searchedTrainer";
        Trainer trainer = new Trainer();
        when(trainerService.findByUsername(username, password, searchedUsername)).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer(username, password, searchedUsername);

        assertNotNull(result);
        verify(trainerService, times(1)).findByUsername(username, password, searchedUsername);
    }

    // ---- Тесты для Training ----
    @Test
    void testCreateTraining() {
        Training training = new Training();
        gymFacade.createTraining(training);
        verify(trainingService, times(1)).create(training);
    }

    @Test
    void testGetTrainingsForTrainee() {
        String username = "user1";
        String password = "pass";
        String traineeUsername = "trainee1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingType = "cardio";
        List<Training> trainings = Arrays.asList(new Training(), new Training());

        when(trainingService.findTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType))
                .thenReturn(trainings);

        List<Training> result = gymFacade.getTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType);

        assertEquals(2, result.size());
        verify(trainingService, times(1)).findTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType);
    }
    @Test
    void testGetTrainingsForTrainer() {
        String username = "trainer1";
        String password = "pass";
        String trainerUsername = "trainer1";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainingType = "strength";

        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingService.findTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, trainingType))
                .thenReturn(trainings);

        List<Training> result = gymFacade.getTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, trainingType);

        assertEquals(2, result.size());
        verify(trainingService, times(1)).findTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, trainingType);
    }

    @Test
    void testUpdateTraineeTrainersList() {
        String username = "user1";
        String password = "pass";
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
        Trainee trainee = new Trainee();

        when(traineeService.updateTrainersList(username, password, trainers)).thenReturn(trainee);

        Trainee result = gymFacade.updateTraineeTrainersList(username, password, trainers);

        assertNotNull(result);
        verify(traineeService, times(1)).updateTrainersList(username, password, trainers);
    }

    @Test
    void testChangeTrainerPassword() {
        Trainer trainer = new Trainer();
        String newPassword = "newPass";

        gymFacade.changeTrainerPassword(trainer, newPassword);

        verify(trainerService, times(1)).changePassword(trainer, newPassword);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        when(trainerService.update(trainer)).thenReturn(trainer);

        Trainer updatedTrainer = gymFacade.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        verify(trainerService, times(1)).update(trainer);
    }

    @Test
    void testActivateOrDeactivateTrainer() {
        Trainer trainer = new Trainer();

        gymFacade.activateOrDeactivateTrainer(trainer);

        verify(trainerService, times(1)).switchActivate(trainer);
    }

}