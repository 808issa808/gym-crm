package org.epam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private String name;
    private TrainingType type;
    private Date date;
    @Autowired
    public void setType(TrainingType type) {
        this.type = type;
    }
    @Autowired
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
    @Autowired
    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    private Duration duration;
    private Trainer trainer;
    private Trainee trainee;
}