package org.epam.data;

import org.epam.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class TrainingDao {
    private  Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Training training) {
        storage.getTrainings().put(storage.nextTrainingId(), training);
    }
    public Optional<Training> findByTraineeId(Long traineeId) {
        return Optional.ofNullable(storage.getTrainings().get(traineeId));
    }

    public Collection<Training> findAll() {
        return storage.getTrainings().values();
    }
}
