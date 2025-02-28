package org.epam.data;

import lombok.extern.slf4j.Slf4j;
import org.epam.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class TrainingDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Training training) {
        Long trainingId = storage.nextTrainingId();
        storage.getTrainings().put(trainingId, training);
        log.info("Saved training with ID: {}", trainingId);
    }

    public Optional<Training> findByTraineeId(Long traineeId) {
        Optional<Training> training = Optional.ofNullable(storage.getTrainings().get(traineeId));
        log.info("Find by trainee ID: {}, found: {}", traineeId, training.isPresent());
        return training;
    }

    public Collection<Training> findAll() {
        Collection<Training> trainings = storage.getTrainings().values();
        log.info("Retrieved all trainings, count: {}", trainings.size());
        return trainings;
    }
}