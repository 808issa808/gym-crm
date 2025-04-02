package org.epam.web.dto.users.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserCredentialsDto;

@Data
public class TraineeUpdateRequest {
    @Valid
    @NotNull
    private UserCredentialsDto userCredentialsDto;
    @Valid
    @NotNull
    private TraineeDto traineeDto;
}