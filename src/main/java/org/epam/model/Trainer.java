package org.epam.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trainers")
public class Trainer extends User {

    @Column(name = "Specialization")
    private String specialization;

    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees;
}
