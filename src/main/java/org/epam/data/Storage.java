package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Storage {
    private final Map<Long, Trainee> trainees = new HashMap<>();
    private final Map<Long, Trainer> trainers = new HashMap<>();
    private final Map<Long, Training> trainings = new HashMap<>();
    private final Map<String, Long> traineeUsernameToId = new HashMap<>();
    private final Map<String, Long> trainerUsernameToId = new HashMap<>();

    private final AtomicLong traineeIdGenerator = new AtomicLong(1);
    private final AtomicLong trainerIdGenerator = new AtomicLong(1);
    private final AtomicLong trainingIdGenerator = new AtomicLong(1);

    public Map<Long, Trainee> getTrainees() {
        return trainees;
    }

    public Map<Long, Trainer> getTrainers() {
        return trainers;
    }

    public Map<String, Long> getTraineeUsernameToId() {
        return traineeUsernameToId;
    }

    public Map<String, Long> getTrainerUsernameToId() {
        return trainerUsernameToId;
    }

    public Map<Long, Training> getTrainings() {
        return trainings;
    }

    public long nextTraineeId() {
        return traineeIdGenerator.getAndIncrement();
    }
    public long nextTrainerId() {
        return trainerIdGenerator.getAndIncrement();
    }
    public long nextTrainingId() {
        return trainingIdGenerator.getAndIncrement();
    }
}
