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

    @Column(name = "training_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
    private TrainingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", referencedColumnName = "id", nullable = false)
    private Trainee trainee;

    @Column(name = "training_date", nullable = false)
    private Date date;

    @Column(name = "training_duration", nullable = false)
    private Integer duration;
}