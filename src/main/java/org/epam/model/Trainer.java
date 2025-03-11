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
@PrimaryKeyJoinColumn(name = "id")
public class Trainer extends User {

    @ManyToOne
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees;
}
