package org.epam.data;


import lombok.RequiredArgsConstructor;
import org.epam.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TraineeDao {
    private final Storage storage;

    public void save(Trainee trainee) {
        storage.getTrainees().put(trainee.getUserId(), trainee);
    }

    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.getTrainees().get(id));
    }

    public Collection<Trainee> findAll() {
        return storage.getTrainees().values();
    }

    public void deleteById(Long id) {
        storage.getTrainees().remove(id);
    }

}
