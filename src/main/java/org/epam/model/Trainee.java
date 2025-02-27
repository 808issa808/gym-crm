package org.epam.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;
    private Long userId;

    public Trainee(Long userId, String firstName, String lastName, String username, String password, boolean active, Date dateOfBirth, String address) {
        super(firstName, lastName, username, password, active);
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}