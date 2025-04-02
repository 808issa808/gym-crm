package org.epam.web.dto.users.trainee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.training.TraineeTrainingsRequest;
import org.epam.web.dto.users.UserCredentialsDto;

@Data
public class findByTraineeUsernameCriteriaRequest {
    @NotNull
    @Valid
    private UserCredentialsDto userCredentialsDto;
    @NotNull
    @Valid
    private TraineeTrainingsRequest traineeTrainingsRequest;
}
