package org.epam.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainerTrainingsRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.trainer.TrainerRegistrationRequest;
import org.epam.web.dto.users.trainer.TrainerUpdateRequest;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainer Management", description = "API for managing trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Operation(
            summary = "Register new trainer",
            description = "Creates a new trainer profile and generates credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer registration data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerRegistrationRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentialsDto register(@Valid @RequestBody TrainerRegistrationRequest registrationDto) {
        return trainerService.create(registrationDto);
    }

    @Operation(
            summary = "Authenticate trainer",
            description = "Validates trainer credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody UserCredentialsDto auth) {
        trainerService.login(auth);
    }

    @Operation(
            summary = "Change password",
            description = "Updates trainer's password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password change request",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangeLoginRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@Valid @RequestBody ChangeLoginRequest changeLoginRequestDto) {
        trainerService.changePassword(changeLoginRequestDto);
    }

    @Operation(
            summary = "Get trainer profile",
            description = "Returns trainer profile details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{username}")
    public TrainerWithListDto getProfile(
            @Valid @RequestBody UserCredentialsDto auth,
            @Parameter(description = "Username of trainer to fetch") @PathVariable("username") String searched) {
        return trainerService.findByUsername(auth, searched);
    }

    @Operation(
            summary = "Update trainer profile",
            description = "Updates trainer profile information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated trainer data with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerUpdateRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot modify another user's data")
    })
    @PutMapping("/{username}")
    public TrainerWithListDto update(@Valid @RequestBody TrainerUpdateRequest request) {
        return trainerService.update(request.getAuth(), request.getTrainerDto());
    }

    @Operation(
            summary = "Toggle activation status",
            description = "Activates or deactivates trainer account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleActivate(@Valid @RequestBody UserCredentialsDto auth) {
        trainerService.switchActivate(auth);
    }

    @Operation(
            summary = "Get trainer trainings",
            description = "Returns filtered list of trainer's trainings",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training search criteria with authentication",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrainerTrainingsRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{username}/trainings")
    public List<GetTrainerTrainingsResponse> findByTrainerUsernameCriteria(
            @Valid @RequestBody TrainerTrainingsRequest request,
            @Parameter(description = "Trainer username") @PathVariable("username") String username) {
        return trainingService.findTrainingsForTrainer(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainee());
    }
}