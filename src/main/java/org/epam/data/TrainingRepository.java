package org.epam.data;

import org.epam.model.Training;

import java.util.Date;
import java.util.List;

public interface TrainingRepository {
    List<Training> findByTraineeUsername(String username);

    List<Training> findByTrainerUsername(String username);

    List<Training> findTrainingsForTraineeByCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType);

    List<Training> findTrainingsForTrainer(String trainerUsername, Date fromDate, Date toDate, String traineeName);

    Training create(Training training);
}