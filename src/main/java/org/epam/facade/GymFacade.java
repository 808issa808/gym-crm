package org.epam.facade;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.service.TraineeService;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public void saveTrainee(Trainee trainee) {
        traineeService.save(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    public Optional<Trainee> findTraineeById(Long id) {
        return traineeService.findById(id);
    }

    public Collection<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public void deleteTraineeById(Long id) {
        traineeService.deleteById(id);
    }

    public void saveTrainer(Trainer trainer) {
        trainerService.save(trainer);
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public Collection<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public void saveTraining(Training training) {
        trainingService.save(training);
    }

    public Optional<Training> findTrainingByTraineeId(Long traineeId) {
        return trainingService.findByTraineeId(traineeId);
    }

    public Collection<Training> findAllTrainings() {
        return trainingService.findAll();
    }
}