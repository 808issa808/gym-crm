package org.epam.web.dto.training;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class GetTrainerTrainingsResponse {
    @NotBlank(message = "Название тренировки обязательно")
    private String name;
    @NotNull(message = "Дата тренировки обязательна")
    private Date date;
    @NotBlank(message = "Тип обязателен")
    private String type;
    @NotNull(message = "Длительность тренировки обязательна")
    private Integer duration;
    @NotBlank(message = "Тренер обязателен")
    private String traineeName;
}
