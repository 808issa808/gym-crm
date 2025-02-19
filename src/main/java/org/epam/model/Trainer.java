package org.epam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Trainer extends User{
    private String specialization;
    private Long userId;

    public Trainer(Long userId, String firstName, String lastName, String username, String password, boolean active, String specialization) {
        super(firstName, lastName, username, password, active);
        this.userId = userId;
        this.specialization = specialization;
    }
}