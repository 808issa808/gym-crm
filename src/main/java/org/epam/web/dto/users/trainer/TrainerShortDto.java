package org.epam.web.dto.users.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserShortDto;

@Data
public class TrainerShortDto extends UserShortDto {
    @NotNull
    private Long specialization;
}
