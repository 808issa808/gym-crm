package org.epam.data;

import lombok.extern.slf4j.Slf4j;
import org.epam.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class TraineeDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void save(Trainee trainee) {
        trainee.setUserId(storage.nextTraineeId());
        storage.getTrainees().put(trainee.getUserId(), trainee);
        storage.getTraineeUsernameToId().put(trainee.getUsername(), trainee.getUserId());
        log.info("Saved trainee: {}", trainee);
    }

    public Trainee update(Trainee trainee) {
        if (!storage.getTrainees().containsKey(trainee.getUserId())) {
            throw new IllegalArgumentException("Trainee with id " + trainee.getUserId() + " does not exist");
        }
        storage.getTrainees().put(trainee.getUserId(), trainee);
        storage.getTraineeUsernameToId().put(trainee.getUsername(), trainee.getUserId());
        log.info("Updated trainee: {}", trainee);
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        log.info("Finding trainee by id: {}", id);
        return Optional.ofNullable(storage.getTrainees().get(id));
    }

    public Collection<Trainee> findAll() {
        log.info("Finding all trainees");
        return storage.getTrainees().values();
    }

    public void deleteById(Long id) {
        if (!storage.getTrainees().containsKey(id)) {
            throw new IllegalArgumentException("Trainee with id " + id + " does not exist and can not be deleted");
        }
        log.info("Deleting trainee by id: {}", id);
        storage.getTraineeUsernameToId().remove(storage.getTrainees().get(id).getUsername());
        storage.getTrainees().remove(id);
    }

    public boolean existsByUsername(String username) {
        boolean exists = storage.getTraineeUsernameToId().containsKey(username);
        log.info("Checking if trainee exists by username '{}': {}", username, exists);
        return exists;
    }
}
