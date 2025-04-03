package org.epam.web.dto.users.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class TraineeRegistrationRequest {

    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;


    private Date dateOfBirth;

    private String address;
}
