package org.epam.data;


import org.epam.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class TraineeDao {
    private  Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
    public void save(Trainee trainee) {
        trainee.setUserId(storage.nextTraineeId());
        storage.getTrainees().put(trainee.getUserId(), trainee);
        storage.getTraineeUsernameToId().put(trainee.getUsername(), trainee.getUserId());
    }
    public Trainee update(Trainee trainee) {
        if (!storage.getTrainees().containsKey(trainee.getUserId())) {
            throw new IllegalArgumentException("Trainee with id " + trainee.getUserId() + " does not exist");
        }
        storage.getTrainees().put(trainee.getUserId(), trainee);
        storage.getTraineeUsernameToId().put(trainee.getUsername(), trainee.getUserId());
        return trainee;
    }


    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.getTrainees().get(id));
    }

    public Collection<Trainee> findAll() {
        return storage.getTrainees().values();
    }

    public void deleteById(Long id) {
        storage.getTraineeUsernameToId().remove(storage.getTrainees().get(id).getUsername());
        storage.getTrainees().remove(id);
    }
    public boolean existsByUsername(String username) {
        return storage.getTraineeUsernameToId().containsKey(username);
    }
}
