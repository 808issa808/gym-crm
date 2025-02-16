package org.epam.model;

import lombok.Data;

@Data
public class Trainer extends User{
    private String specialization;
    private Long userId;
}
