package org.epam.web.controller.swaggerInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.epam.model.TrainingType;
import org.epam.web.dto.users.UserCredentialsDto;

import java.util.List;

@Tag(name = "Training Type Management", description = "API for managing training types")
public interface TrainingTypeApi {

    @Operation(
            summary = "Get all training types",
            description = "Returns list of all available training types",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    List<TrainingType> getAll(UserCredentialsDto auth);
}