package org.epam.model;

import lombok.Data;

import java.util.Date;

@Data
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;
    private Long userId;
}
