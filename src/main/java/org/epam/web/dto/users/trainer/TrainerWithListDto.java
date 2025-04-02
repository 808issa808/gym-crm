package org.epam.web.dto.users.trainer;

import lombok.Data;
import org.epam.web.dto.users.UserShortDto;

import java.util.List;

@Data
public class TrainerWithListDto extends TrainerDto{
    private List<UserShortDto> traineeShortDtoList;
}
