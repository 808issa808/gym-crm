package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    int countByUsernamePrefix(String usernamePrefix);

    Optional<Trainee> findByUsername(String username);

    List<Trainer> getNotMineTrainersByUsername(String username);

    Trainee save(Trainee trainee);

    void delete(Trainee trainee);
}