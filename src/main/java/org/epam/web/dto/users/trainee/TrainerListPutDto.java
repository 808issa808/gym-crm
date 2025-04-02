package org.epam.web.dto.users.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TrainerListPutDto {
    @NotEmpty(message = "Trainer username list cannot be empty")
    private List<@NotBlank(message = "Trainer username cannot be blank") String> trainerUsernameList;
}
