package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Training;
import org.epam.util.Authenticator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepositoryImpl trainingRepository;
    private final TraineeRepositoryImpl traineeRepository;
    private final TrainerRepositoryImpl trainerRepository;

    @Transactional
    public void create(Training training) {
        trainingRepository.create(training);
    }

    public List<Training> findByTraineeUsername(String username, String password) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return trainingRepository.findByTraineeUsername(username);
    }

    public List<Training> findByTrainerUsername(String username, String password) {
        Authenticator.authenticateUser(username, password, trainerRepository::findByUsername);
        return trainingRepository.findByTrainerUsername(username);
    }

    public List<Training> findTrainingsForTrainee(String username, String password, String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        return trainingRepository.findTrainingsByCriteria(traineeUsername, fromDate, toDate, trainerName,"trainee", trainingType);
    }

    public List<Training> findTrainingsForTrainer(String username, String password, String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        Authenticator.authenticateUser(username, password, trainerRepository::findByUsername);
        return trainingRepository.findTrainingsByCriteria(trainerUsername, fromDate, toDate, traineeName,"trainer",null);
    }
}
