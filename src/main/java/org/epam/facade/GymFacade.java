package org.epam.facade;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    // ------------------- Trainee operations -------------------

    public void createTrainee(Trainee trainee) {
        traineeService.create(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    public Trainee findTraineeByUsername(String username, String password, String searchedUsername) {
        return traineeService.findByUsername(username, password, searchedUsername);
    }

    public List<Trainer> getNotMineTrainersByUsername(String username, String password) {
        return traineeService.getNotMineTrainersByUsername(username, password);
    }

    public Trainee updateTrainersList(String username, String password, List<Trainer> updatedTrainers) {
        return traineeService.updateTrainersList(username, password, updatedTrainers);
    }

    public Trainee changeTraineePassword(Trainee trainee, String password) {
        return traineeService.changePassword(trainee, password);
    }

    public void switchTraineeActivate(Trainee trainee) {
        traineeService.switchActivate(trainee);
    }

    public void deleteTrainee(String username, String password) {
        traineeService.deleteTraineeByUsername(username, password);
    }

    // ------------------- Trainer operations -------------------

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.update(trainer);
    }

    public Trainer findTrainerByUsername(String username, String password, String searchedUsername) {
        return trainerService.findByUsername(username, password, searchedUsername);
    }

    public Trainer changeTrainerPassword(Trainer trainer, String password) {
        return trainerService.changePassword(trainer, password);
    }

    public void switchTrainerActivate(Trainer trainer) {
        trainerService.switchActivate(trainer);
    }

    // ------------------- Training operations -------------------

    public void createTraining(Training training) {
        trainingService.create(training);
    }

    public List<Training> findTrainingsByTraineeUsername(String username, String password) {
        return trainingService.findByTraineeUsername(username, password);
    }

    public List<Training> findTrainingsByTrainerUsername(String username, String password) {
        return trainingService.findByTrainerUsername(username, password);
    }

    public List<Training> findTrainingsForTrainee(String username, String password, String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingService.findTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> findTrainingsForTrainer(String username, String password, String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingService.findTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, traineeName);
    }
}