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

    // ---- Тренируемые (Trainees) ----
    public void createTrainee(Trainee trainee) {
        traineeService.create(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    public Trainee getTrainee(String username, String password, String searchedUsername) {
        return traineeService.findByUsername(username, password, searchedUsername);
    }

    public void deleteTrainee(String username, String password) {
        traineeService.deleteTraineeByUsername(username, password);
    }

    public void changeTraineePassword(Trainee trainee, String newPassword) {
        traineeService.changePassword(trainee, newPassword);
    }

    public void activateOrDeactivateTrainee(Trainee trainee) {
        traineeService.switchActivate(trainee);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String username, String password) {
        return traineeService.getNotMineTrainersByUsername(username, password);
    }

    public Trainee updateTraineeTrainersList(String username, String password, List<Trainer> updatedTrainers) {
        return traineeService.updateTrainersList(username, password, updatedTrainers);
    }

    // ---- Тренеры (Trainers) ----
    public void createTrainer(Trainer trainer) {
        trainerService.create(trainer);
    }

    public Trainer getTrainer(String username, String password, String searchedUsername) {
        return trainerService.findByUsername(username, password, searchedUsername);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.update(trainer);
    }

    public void changeTrainerPassword(Trainer trainer, String newPassword) {
        trainerService.changePassword(trainer, newPassword);
    }

    public void activateOrDeactivateTrainer(Trainer trainer) {
        trainerService.switchActivate(trainer);
    }

    // ---- Тренировки (Trainings) ----
    public void createTraining(Training training) {
        trainingService.create(training);
    }

    public List<Training> getTrainingsForTrainee(String username, String password, String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        return trainingService.findTrainingsForTrainee(username, password, traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainingsForTrainer(String username, String password, String trainerUsername, Date fromDate, Date toDate, String trainingType) {
        return trainingService.findTrainingsForTrainer(username, password, trainerUsername, fromDate, toDate, trainingType);
    }
}