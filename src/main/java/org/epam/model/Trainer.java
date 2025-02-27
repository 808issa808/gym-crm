package org.epam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class Trainer extends User{
    private TrainingType specialization;
    private Long userId;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, boolean active, TrainingType specialization) {
        super(firstName, lastName, username, password, active);
        this.userId = userId;
        this.specialization = specialization;
    }
}