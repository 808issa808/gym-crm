package org.epam.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TrainingName", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "TrainingTypeId", referencedColumnName = "id", nullable = false)
    private TrainingType type;

    @ManyToOne
    @JoinColumn(name = "TrainerId", referencedColumnName = "id", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "TraineeId", referencedColumnName = "id", nullable = false)
    private Trainee trainee;

    @Column(name = "TrainingDate", nullable = false)
    private Date date;

    @Column(name = "TrainingDuration", nullable = false)
    private Integer duration;
}