package org.epam.web.controller.swaggerInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.TraineeTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.*;
import org.epam.web.dto.users.trainer.TrainerShortDto;

import java.util.List;

@Tag(name = "Trainee Management", description = "API for managing trainees")
public interface TraineeApi {

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
    String register(TraineeRegistrationRequest registrationDto);

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
    String login(UserCredentialsDto auth);

    @Operation(summary = "Change password",
            description = "Updates trainee's password",
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
    void changeLogin(ChangeLoginRequest changeLoginRequestDto);

    @Operation(summary = "Get trainee profile",
            description = "Returns trainee profile details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    TraineeWithListDto getProfile(
            UserCredentialsDto auth,
            @Parameter(description = "Username of trainee to fetch") String username);

    @Operation(summary = "Update trainee profile",
            description = "Updates trainee profile information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated trainee data with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeUpdateRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot modify another user's data")
    })
    TraineeWithListDto update(TraineeUpdateRequest request);

    @Operation(summary = "Delete trainee",
            description = "Deletes trainee profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delete trainee data with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void delete(UserCredentialsDto auth);

    @Operation(summary = "Get available trainers",
            description = "Returns list of trainers not assigned to this trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    List<TrainerShortDto> getNotMineTrainers(UserCredentialsDto loginDto);

    @Operation(summary = "Update trainers list",
            description = "Updates list of trainers assigned to trainee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated trainers list with credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTrainersRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    List<TrainerShortDto> updateTrainersList(UpdateTrainersRequest request);

    @Operation(summary = "Toggle activation status",
            description = "Activates or deactivates trainee account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCredentialsDto.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void toggleActivate(UserCredentialsDto auth);

    @Operation(summary = "Get trainee trainings",
            description = "Returns filtered list of trainee's trainings",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training search criteria with authentication",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TraineeTrainingsRequest.class)
                    )
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    List<GetTraineeTrainingsResponse> findByTraineeUsernameCriteria(
            TraineeTrainingsRequest request,
            @Parameter(description = "Trainee username") String username);
}