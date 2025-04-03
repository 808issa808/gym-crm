package org.epam.web.dto.users.trainer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserCredentialsDto;

@Data
public class TrainerUpdateRequest {
    @NotNull
    @Valid
    private UserCredentialsDto auth;
    @NotNull
    @Valid
    private TrainerDto trainerDto;
}
