package org.epam.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.model.TrainingType;
import org.epam.service.TrainingTypeService;
import org.epam.web.dto.users.UserCredentialsDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-types")
@RequiredArgsConstructor
@Tag(name = "Training Type Management", description = "API for managing training types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @Operation(
            summary = "Get all training types",
            description = "Returns list of all available training types",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public List<TrainingType> getAll(@Valid @RequestBody UserCredentialsDto auth) {
        return trainingTypeService.getAll(auth);
    }
}