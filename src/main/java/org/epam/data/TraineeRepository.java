package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    boolean existsByUsername(String username);

    Optional<Trainee> findByUsername(String username);

    List<Trainer> getNotMineTrainersByUsername(String username);

    Trainee create(Trainee trainee);

    Trainee update(Trainee trainee);

    Trainee changePassword(Trainee trainee, String password);

    Trainee updateTrainersList(Trainee trainee, List<Trainer> updatedTrainers);

    void switchActivate(String username);

    void deleteByUsername(String Username);
}