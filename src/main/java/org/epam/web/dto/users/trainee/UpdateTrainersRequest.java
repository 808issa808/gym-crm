package org.epam.web.dto.users.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserCredentialsDto;

@Data
public class UpdateTrainersRequest {
    @Valid
    @NotNull
    private UserCredentialsDto loginDto;
    @Valid
    @NotNull
    private TrainerListPutDto updateUsernames;
}
