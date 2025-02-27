package org.epam.data;

import lombok.RequiredArgsConstructor;
import org.epam.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TrainerDao {
    private final Storage storage;

    public void save(Trainer trainer) {
        storage.getTrainers().put(trainer.getUserId(), trainer);
    }

    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.getTrainers().get(id));
    }

    public Collection<Trainer> findAll() {
        return storage.getTrainers().values();
    }

    public boolean existsByUsername(String username) {
        return storage.getTrainers().values().stream()
                .anyMatch(trainer -> trainer.getUsername().equals(username));
    }

}
