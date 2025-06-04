package org.epam.data;

import org.epam.model.Training;

import java.util.Date;
import java.util.List;

public interface TrainingRepository {
    List<Training> findByTraineeUsername(String username);

    List<Training> findByTrainerUsername(String username);
    Training create(Training training);
}