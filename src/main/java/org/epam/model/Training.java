package org.epam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private String name;
    private String type;
    private Date date;
    private Duration duration;
    private Trainer trainer;
    private Trainee trainee;
}