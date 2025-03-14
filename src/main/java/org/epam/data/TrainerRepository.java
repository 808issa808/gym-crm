package org.epam.data;

import jakarta.transaction.Transactional;
import org.epam.model.Trainer;

import java.util.Optional;

public interface TrainerRepository {
//    boolean existsByUsername(String username);

    int countByUsernamePrefix(String usernamePrefix);

    Optional<Trainer> findByUsername(String username);

    Trainer save( Trainer  trainer);

//    Trainer create(Trainer trainer);

//    Trainer changePassword(Trainer trainer, String password);

//    Trainer update(Trainer trainer);

//    void switchActivate(String username);
}
