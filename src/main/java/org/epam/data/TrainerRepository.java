package org.epam.data;

import org.epam.model.Trainer;

import java.util.Optional;

public interface TrainerRepository {

    Optional<Trainer> findByUsername(String username);

    Trainer save(Trainer trainer);
}
