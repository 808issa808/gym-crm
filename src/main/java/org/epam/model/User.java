package org.epam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @NotBlank(message = "Логин не может быть пустым")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("true")
    private boolean isActive;

    @PrePersist
    protected void prePersist() {
        isActive = true;
    }
}