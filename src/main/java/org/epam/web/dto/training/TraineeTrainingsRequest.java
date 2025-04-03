package org.epam.web.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserCredentialsDto;

import java.util.Date;

@Data
public class TraineeTrainingsRequest {
    @NotNull
    @Valid
    private UserCredentialsDto auth;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date periodFrom;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date periodTo;
    private String trainer;
    private String trainingType;
}
