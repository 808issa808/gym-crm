package org.epam.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.service.TrainingService;
import org.epam.web.dto.training.TrainerTrainingsRequest;
import org.epam.web.dto.training.TrainingCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Training Management", description = "API for managing trainings")
public class TrainingController {
    private final TrainingService trainingService;

    @Operation(
            summary = "Create new training",
            description = "Creates a new training session between trainee and trainer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training search criteria with authentication",
                    required = true, content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainerTrainingsRequest.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@Valid @RequestBody TrainingCreateDto trainingCreateDto) {
        trainingService.create(trainingCreateDto);
    }
}