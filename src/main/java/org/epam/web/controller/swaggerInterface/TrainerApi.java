package org.epam.web.controller.swaggerInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainerTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainer.*;

import java.util.List;


@Tag(name = "Trainer Management", description = "API for managing trainers")
public interface TrainerApi {

    @Operation(summary = "Register new trainer",
            description = "Creates a new trainer profile and generates credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = TrainerRegistrationRequest.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    UserCredentialsDto register(TrainerRegistrationRequest registrationDto);

    @Operation(summary = "Authenticate trainer",
            description = "Validates trainer credentials",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ))
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    void login(UserCredentialsDto auth);

    @Operation(summary = "Change password",
            description = "Updates trainer's password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ChangeLoginRequest.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void changeLogin(ChangeLoginRequest changeLoginRequestDto);

    @Operation(summary = "Get trainer profile",
            description = "Returns trainer profile details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    TrainerWithListDto getProfile(
            UserCredentialsDto auth,
            @Parameter(description = "Username of trainer to fetch") String username);

    @Operation(summary = "Update trainer profile",
            description = "Updates trainer profile information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = TrainerUpdateRequest.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot modify another user's data")
    })
    TrainerWithListDto update(TrainerUpdateRequest request);

    @Operation(summary = "Toggle activation status",
            description = "Activates or deactivates trainer account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void toggleActivate(UserCredentialsDto auth);

    @Operation(summary = "Get trainer trainings",
            description = "Returns filtered list of trainer's trainings",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = TrainerTrainingsRequest.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    List<GetTrainerTrainingsResponse> findByTrainerUsernameCriteria(
            TrainerTrainingsRequest request,
            @Parameter(description = "Trainer username") String username);
}