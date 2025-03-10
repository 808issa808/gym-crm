package org.epam.data;

import org.epam.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    public boolean existsByUsername(String username);
    Optional<Trainer> findByUsername(String username);
}
