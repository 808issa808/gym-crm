package org.epam.web.controller.swaggerInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.epam.web.dto.training.TrainingCreateDto;
import org.epam.web.dto.training.TrainerTrainingsRequest;

@Tag(name = "Training Management", description = "API for managing trainings")
public interface TrainingApi {

    @Operation(
            summary = "Create new training",
            description = "Creates a new training session between trainee and trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training search criteria with authentication",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerTrainingsRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    void add(TrainingCreateDto trainingCreateDto);
}