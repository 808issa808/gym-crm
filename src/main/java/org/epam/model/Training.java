package org.epam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название тренировки обязательно")
    @Column(name = "training_name", nullable = false)
    private String name;

    @NotNull(message = "Тип тренировки обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
    private TrainingType type;

    @NotNull(message = "Тренер обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
    private Trainer trainer;

    @NotNull(message = "Ученик обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", referencedColumnName = "id", nullable = false)
    private Trainee trainee;

    @NotNull(message = "Дата тренировки обязательна")
    @Column(name = "training_date", nullable = false)
    private Date date;

    @NotNull(message = "Длительность тренировки обязательна")
    @Column(name = "training_duration", nullable = false)
    private Integer duration;
}