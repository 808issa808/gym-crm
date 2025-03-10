package org.epam.data;

import org.epam.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training,Long> {
    List<Training> findByTraineeUsername(String username);
    List<Training> findByTrainerUsername(String username);
}
