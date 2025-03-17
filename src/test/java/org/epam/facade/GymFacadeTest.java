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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @InjectMocks
    private GymFacade gymFacade;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;
    private List<Trainer> trainers;
    private List<Training> trainings;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainer = new Trainer();
        training = new Training();
        trainers = new ArrayList<>();
        trainings = new ArrayList<>();
    }

    // ------------------- Trainee operations -------------------

    @Test
    void testCreateTrainee() {
        gymFacade.createTrainee(trainee);
        verify(traineeService, times(1)).create(trainee);
    }

    @Test
    void testUpdateTrainee() {
        when(traineeService.update(trainee)).thenReturn(trainee);
        Trainee updatedTrainee = gymFacade.updateTrainee(trainee);
        assertEquals(trainee, updatedTrainee);
    }

    @Test
    void testFindTraineeByUsername() {
        when(traineeService.findByUsername("user", "password", "searchedUser")).thenReturn(trainee);
        Trainee foundTrainee = gymFacade.findTraineeByUsername("user", "password", "searchedUser");
        assertEquals(trainee, foundTrainee);
    }

    @Test
    void testGetNotMineTrainersByUsername() {
        when(traineeService.getNotMineTrainersByUsername("user", "password")).thenReturn(trainers);
        List<Trainer> result = gymFacade.getNotMineTrainersByUsername("user", "password");
        assertEquals(trainers, result);
    }

    @Test
    void testUpdateTrainersList() {
        when(traineeService.updateTrainersList("user", "password", trainers)).thenReturn(trainee);
        Trainee updatedTrainee = gymFacade.updateTrainersList("user", "password", trainers);
        assertEquals(trainee, updatedTrainee);
    }

    @Test
    void testChangeTraineePassword() {
        when(traineeService.changePassword(trainee, "newPassword")).thenReturn(trainee);
        Trainee updatedTrainee = gymFacade.changeTraineePassword(trainee, "newPassword");
        assertEquals(trainee, updatedTrainee);
    }

    @Test
    void testSwitchTraineeActivate() {
        gymFacade.switchTraineeActivate(trainee);
        verify(traineeService, times(1)).switchActivate(trainee);
    }

    @Test
    void testDeleteTrainee() {
        gymFacade.deleteTrainee("user", "password");
        verify(traineeService, times(1)).deleteTraineeByUsername("user", "password");
    }

    // ------------------- Trainer operations -------------------

    @Test
    void testCreateTrainer() {
        gymFacade.createTrainer(trainer);
        verify(trainerService, times(1)).create(trainer);
    }

    @Test
    void testUpdateTrainer() {
        when(trainerService.update(trainer)).thenReturn(trainer);
        Trainer updatedTrainer = gymFacade.updateTrainer(trainer);
        assertEquals(trainer, updatedTrainer);
    }

    @Test
    void testFindTrainerByUsername() {
        when(trainerService.findByUsername("user", "password", "searchedUser")).thenReturn(trainer);
        Trainer foundTrainer = gymFacade.findTrainerByUsername("user", "password", "searchedUser");
        assertEquals(trainer, foundTrainer);
    }

    @Test
    void testChangeTrainerPassword() {
        when(trainerService.changePassword(trainer, "newPassword")).thenReturn(trainer);
        Trainer updatedTrainer = gymFacade.changeTrainerPassword(trainer, "newPassword");
        assertEquals(trainer, updatedTrainer);
    }

    @Test
    void testSwitchTrainerActivate() {
        gymFacade.switchTrainerActivate(trainer);
        verify(trainerService, times(1)).switchActivate(trainer);
    }

    // ------------------- Training operations -------------------

    @Test
    void testCreateTraining() {
        gymFacade.createTraining(training);
        verify(trainingService, times(1)).create(training);
    }

    @Test
    void testFindTrainingsByTraineeUsername() {
        when(trainingService.findByTraineeUsername("user", "password")).thenReturn(trainings);
        List<Training> result = gymFacade.findTrainingsByTraineeUsername("user", "password");
        assertEquals(trainings, result);
    }

    @Test
    void testFindTrainingsByTrainerUsername() {
        when(trainingService.findByTrainerUsername("user", "password")).thenReturn(trainings);
        List<Training> result = gymFacade.findTrainingsByTrainerUsername("user", "password");
        assertEquals(trainings, result);
    }

    @Test
    void testFindTrainingsForTrainee() {
        Date from = new Date();
        Date toDate = new Date();
        when(trainingService.findTrainingsForTrainee("user", "password", "traineeUsername", from, toDate, "trainerName", "trainingType")).thenReturn(trainings);
        List<Training> result = gymFacade.findTrainingsForTrainee("user", "password", "traineeUsername", from, toDate, "trainerName", "trainingType");
        assertEquals(trainings, result);
    }

    @Test
    void testFindTrainingsForTrainer() {
        Date from = new Date();
        Date toDate = new Date();
        when(trainingService.findTrainingsForTrainer("user", "password", "trainerUsername", from, toDate, "traineeName")).thenReturn(trainings);
        List<Training> result = gymFacade.findTrainingsForTrainer("user", "password", "trainerUsername", from, toDate, "traineeName");
        assertEquals(trainings, result);
    }
}