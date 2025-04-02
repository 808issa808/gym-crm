package org.epam.web.dto.users.trainee;

import lombok.Data;
import org.epam.web.dto.users.trainer.TrainerShortDto;

import java.util.List;

@Data
public class TraineeWithListDto extends TraineeDto{
    private List<TrainerShortDto> trainerShortDtoList;
}
