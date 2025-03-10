package org.epam.data;

import org.epam.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
     public boolean existsByUsername(String username);
     Optional<Trainee> findByUsername(String username);
}
