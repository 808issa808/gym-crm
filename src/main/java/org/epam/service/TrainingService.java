package org.epam.service;

import lombok.RequiredArgsConstructor;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.Training;
import org.epam.service.workload.WorkloadService;
import org.epam.service.workload.dto.TrainerSummary;
import org.epam.service.workload.dto.UpdateReport;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainingCreateDto;
import org.epam.web.exp.ForbiddenException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepositoryImpl trainingRepository;
    private final TraineeRepositoryImpl traineeRepository;
    private final TrainerRepositoryImpl trainerRepository;
    private final ModelMapper modelMapper;
    private final WorkloadService workloadService;

    @Transactional
    public void create(TrainingCreateDto trainingCreateDto) {
        var myName = SecurityContextHolder.getContext().getAuthentication().getName();

        var trainee = traineeRepository.findByUsername(trainingCreateDto.getTrainee())
                .orElseThrow(() -> new IllegalArgumentException("No trainee with given username : " + trainingCreateDto.getTrainee() + " exists."));
        var trainer = trainerRepository.findByUsername(myName)
                .orElseThrow(() -> new IllegalArgumentException("There is no trainer with username: " + myName));

        Training training = modelMapper.map(trainingCreateDto, Training.class);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setType(trainer.getSpecialization());

        trainingRepository.create(training);

        var updateReport = modelMapper.map(training, UpdateReport.class);
        updateReport.setAction(UpdateReport.Action.ADD);

        workloadService.updateTrainerSummary(updateReport);
    }

    @Transactional
    public void deleteTrainingById(Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        var training = trainingRepository.findTrainingById(id)
                .orElseThrow(() -> new NoSuchElementException("Training with id: " + id + " not found"));

        if (!training.getTrainer().getUsername().equals(currentUsername)) {
            throw new ForbiddenException("Not allowed to delete not your training");
        }

        trainingRepository.delete(training);

        var updateReport = modelMapper.map(training, UpdateReport.class);
        updateReport.setAction(UpdateReport.Action.DELETE);

        workloadService.updateTrainerSummary(updateReport);
    }


    public List<GetTraineeTrainingsResponse> findTrainingsForTrainee(Date fromDate, Date toDate, String trainerName, String trainingType) {
        var myName = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Training> trainings = trainingRepository.findTrainingsByCriteria(myName, fromDate, toDate, trainerName, "trainee", trainingType);

        return trainings.stream().map(x -> modelMapper.map(x, GetTraineeTrainingsResponse.class)).collect(Collectors.toList());
    }

    public List<GetTrainerTrainingsResponse> findTrainingsForTrainer(Date fromDate, Date toDate, String traineeName) {
        var myName = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Training> trainings = trainingRepository.findTrainingsByCriteria(myName, fromDate, toDate, traineeName, "trainer", null);

        return trainings.stream().map(x -> modelMapper.map(x, GetTrainerTrainingsResponse.class)).collect(Collectors.toList());
    }

    public TrainerSummary getSummary(String username) {
        return workloadService.getSummary(username);
    }
}