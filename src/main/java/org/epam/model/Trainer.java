package org.epam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@Component
public class Trainer extends User{
    private TrainingType specialization;
    private Long userId;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, boolean active, TrainingType specialization) {
        super(firstName, lastName, username, password, active);
        this.userId = userId;
        this.specialization = specialization;
    }
}