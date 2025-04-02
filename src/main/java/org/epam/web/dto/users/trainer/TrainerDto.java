package org.epam.web.dto.users.trainer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.epam.web.dto.users.UserDto;

@Data
public class TrainerDto extends UserDto {
    @NotNull
    private Long specialization;
}
