package org.epam.data;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class TrainerDao {
    private  Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
    public void save(Trainer trainer) {
        trainer.setUserId(storage.nextTrainerId());
        storage.getTrainers().put(trainer.getUserId(), trainer);
        storage.getTrainerUsernameToId().put(trainer.getUsername(), trainer.getUserId());
    }

    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.getTrainers().get(id));
    }

    public Collection<Trainer> findAll() {
        return storage.getTrainers().values();
    }

    public boolean existsByUsername(String username) {
        return storage.getTrainerUsernameToId().containsKey(username);
    }
}