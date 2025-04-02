package org.epam.web.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserCredentialsDto;

import java.util.Date;

@Data
public class TrainingCreateDto {
    @NotNull
    @Valid
    private UserCredentialsDto auth;
    @NotBlank(message = "Название тренировки обязательно")
    private String name;

    @NotBlank(message = "Тренер обязателен")
    private String trainer;

    @NotBlank(message = "Ученик обязателен")
    private String trainee;

    @NotNull(message = "Дата тренировки обязательна")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @NotNull(message = "Длительность тренировки обязательна")
    private Integer duration;
}
