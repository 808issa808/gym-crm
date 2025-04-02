package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Training;
import org.epam.util.Authenticator;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainingCreateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepositoryImpl trainingRepository;
    private final TraineeRepositoryImpl traineeRepository;
    private final TrainerRepositoryImpl trainerRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void create(TrainingCreateDto trainingCreateDto) {
        Authenticator.authenticateUser(trainingCreateDto.getAuth().getUsername(), trainingCreateDto.getAuth().getPassword(), traineeRepository::findByUsername);

        var trainee = traineeRepository.findByUsername(trainingCreateDto.getTrainee())
                .orElseThrow(() -> new IllegalArgumentException("No trainee with given username : " + trainingCreateDto.getTrainee() + " exists."));
        var trainer = trainerRepository.findByUsername(trainingCreateDto.getTrainer())
                .orElseThrow(() -> new IllegalArgumentException("There is no trainer with username: " + trainingCreateDto.getTrainer()));

        Training training = modelMapper.map(trainingCreateDto, Training.class);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainer.getSpecialization());

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

    public List<GetTraineeTrainingsResponse> findTrainingsForTrainee(String username, String password, String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        Authenticator.authenticateUser(username, password, traineeRepository::findByUsername);
        List<Training> trainings = trainingRepository.findTrainingsByCriteria(traineeUsername, fromDate, toDate, trainerName, "trainee", trainingType);

        return trainings.stream().map(x -> modelMapper.map(x, GetTraineeTrainingsResponse.class)).collect(Collectors.toList());
    }

    public List<GetTrainerTrainingsResponse> findTrainingsForTrainer(String username, String password, String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        Authenticator.authenticateUser(username, password, trainerRepository::findByUsername);
        List<Training> trainings = trainingRepository.findTrainingsByCriteria(trainerUsername, fromDate, toDate, traineeName, "trainer", null);

        return trainings.stream().map(x -> modelMapper.map(x, GetTrainerTrainingsResponse.class)).collect(Collectors.toList());
    }
}