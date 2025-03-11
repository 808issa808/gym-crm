package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository {
    boolean existsByUsername(String username);
    Trainee create(Trainee trainee);
    Optional<Trainee> findByUsername(String username);
    Trainee changePassword(String password);
    Trainee update(Trainee trainee);
    Trainee switchActivate(Trainee trainee);
    List<Trainer> getNotMineTrainersByUsername(String username);
    Trainee updateTrainersList(Trainee trainee, List<Trainer> updatedTrainers);
}
