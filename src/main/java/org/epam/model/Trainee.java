package org.epam.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trainees")
@PrimaryKeyJoinColumn(name = "id")
public class Trainee extends User {

    @Column(name = "DateOfBirth")
    private Date dateOfBirth;

    @Column(name = "Address")
    private String address;

    @ManyToMany
    @JoinTable(
            name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers;
}