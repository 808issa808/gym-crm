package org.epam.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
@PrimaryKeyJoinColumn(name = "id")
public class Trainer extends User {

    @NotNull(message = "Специализация обязательна")
    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
    private TrainingType specialization;

    @Valid
    @ManyToMany(mappedBy = "trainers", cascade = CascadeType.ALL)
    private List<Trainee> trainees;
}