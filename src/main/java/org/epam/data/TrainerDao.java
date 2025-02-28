package org.epam.data;

import lombok.extern.slf4j.Slf4j;
import org.epam.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDao {
    private Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
        log.info("Storage initialized in TrainerDao");
    }

    public void save(Trainer trainer) {
        trainer.setUserId(storage.nextTrainerId());
        storage.getTrainers().put(trainer.getUserId(), trainer);
        storage.getTrainerUsernameToId().put(trainer.getUsername(), trainer.getUserId());
        log.info("Trainer saved: id={}, username={}", trainer.getUserId(), trainer.getUsername());
    }

    public Optional<Trainer> findById(Long id) {
        Optional<Trainer> trainer = Optional.ofNullable(storage.getTrainers().get(id));
        log.info("Find by id: id={}, found={}", id, trainer.isPresent());
        return trainer;
    }

    public Collection<Trainer> findAll() {
        Collection<Trainer> trainers = storage.getTrainers().values();
        log.info("Find all trainers: count={}", trainers.size());
        return trainers;
    }

    public boolean existsByUsername(String username) {
        boolean exists = storage.getTrainerUsernameToId().containsKey(username);
        log.info("Exists by username: username={}, exists={}", username, exists);
        return exists;
    }
}
