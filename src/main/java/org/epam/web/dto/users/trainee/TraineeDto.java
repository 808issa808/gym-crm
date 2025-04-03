package org.epam.web.dto.users.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.epam.web.dto.users.UserDto;

import java.util.Date;

@Data
public class TraineeDto extends UserDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateOfBirth;

    private String address;
}
