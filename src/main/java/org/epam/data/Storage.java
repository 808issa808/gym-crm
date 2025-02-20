package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Storage {
    private final Map<Long, Trainee> trainees = new HashMap<>();
    private final Map<Long, Trainer> trainers = new HashMap<>();
    private final Map<Long, Training> trainings = new HashMap<>();

    public Map<Long, Trainee> getTrainees() {
        return trainees;
    }

    public Map<Long, Trainer> getTrainers() {
        return trainers;
    }

    public Map<Long, Training> getTrainings() {
        return trainings;
    }
}
