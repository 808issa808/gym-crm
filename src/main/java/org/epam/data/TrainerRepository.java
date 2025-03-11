package org.epam.data;

import org.epam.model.Trainer;

import java.util.Optional;

public interface TrainerRepository {
    boolean existsByUsername(String username);
    Trainer create(Trainer trainer);
    Optional<Trainer> findByUsername(String username);
    Trainer changePassword(String password);
    Trainer update(Trainer trainer);
    Trainer switchActivate(Trainer trainer);


}
