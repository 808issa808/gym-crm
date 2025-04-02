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
import org.epam.service.TraineeService;
import org.epam.service.TrainingService;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.TraineeTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.*;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainees")
@Tag(name = "Trainee Management", description = "API for managing trainees")
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Operation(summary = "Register new trainee",
            description = "Creates a new trainee profile and generates credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainee registration data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeRegistrationRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentialsDto register(@Valid @RequestBody TraineeRegistrationRequest registrationDto) {
        return traineeService.create(registrationDto);
    }

    @Operation(summary = "Authenticate trainee",
            description = "Validates trainee credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody UserCredentialsDto auth) {
        traineeService.login(auth);
    }

    @Operation(
            summary = "Change password",
            description = "Updates trainee's password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password change request",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangeLoginRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@Valid @RequestBody ChangeLoginRequest changeLoginRequestDto) {
        traineeService.changePassword(changeLoginRequestDto);
    }

    @Operation(
            summary = "Get trainee profile",
            description = "Returns trainee profile details",
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
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{username}")
    public TraineeWithListDto getProfile(
            @Valid @RequestBody UserCredentialsDto auth,
            @Parameter(description = "Username of trainee to fetch") @PathVariable("username") String username) {
        return traineeService.findByUsername(auth, username);
    }

    @Operation(
            summary = "Update trainee profile",
            description = "Updates trainee profile information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated trainee data with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeUpdateRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot modify another user's data")
    })
    @PutMapping("/{username}")
    public TraineeWithListDto update(@Valid @RequestBody TraineeUpdateRequest request) {
        return traineeService.update(request.getUserCredentialsDto(), request.getTraineeDto());
    }

    @Operation(summary = "Delete trainee",
            description = "Deletes trainee profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delete trainee data with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody UserCredentialsDto auth) {
        traineeService.deleteTraineeByUsername(auth);
    }

    @Operation(
            summary = "Get available trainers",
            description = "Returns list of trainers not assigned to this trainee",
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
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{username}/not-assigned-trainers")
    public List<TrainerShortDto> getNotMineTrainers(@Valid @RequestBody UserCredentialsDto loginDto) {
        return traineeService.getNotMineTrainersByUsername(loginDto);
    }

    @Operation(
            summary = "Update trainers list",
            description = "Updates list of trainers assigned to trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated trainers list with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTrainersRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{username}/trainers-list")
    public List<TrainerShortDto> updateTrainersList(@Valid @RequestBody UpdateTrainersRequest request) {
        return traineeService.updateTrainersList(request.getLoginDto(), request.getUpdateUsernames());
    }

    @Operation(
            summary = "Toggle activation status",
            description = "Activates or deactivates trainee account",
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
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleActivate(@Valid @RequestBody UserCredentialsDto auth) {
        traineeService.switchActivate(auth);
    }

    @Operation(
            summary = "Get trainee trainings",
            description = "Returns filtered list of trainee's trainings",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training search criteria with authentication",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeTrainingsRequest.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{username}/trainings")
    public List<GetTraineeTrainingsResponse> findByTraineeUsernameCriteria(
            @Valid @RequestBody TraineeTrainingsRequest request,
            @Parameter(description = "Trainee username") @PathVariable("username") String username) {
        return trainingService.findTrainingsForTrainee(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainer(),
                request.getTrainingType());
    }
}