package org.epam.data;

import lombok.RequiredArgsConstructor;
import org.epam.model.Training;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TrainingDao {
    private final Storage storage;

    public void save(Training training) {
        storage.getTrainings().put(training.getTrainee().getUserId(), training);
    }

    public Optional<Training> findByTraineeId(Long traineeId) {
        return Optional.ofNullable(storage.getTrainings().get(traineeId));
    }

    public Collection<Training> findAll() {
        return storage.getTrainings().values();
    }
}
