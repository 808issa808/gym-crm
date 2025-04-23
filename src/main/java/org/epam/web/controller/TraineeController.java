package org.epam.web.controller;

import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Parameter;
import io.micrometer.core.instrument.Counter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.service.TraineeService;
import org.epam.service.TrainingService;
import org.epam.web.controller.swaggerInterface.TraineeApi;
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
public class TraineeController implements TraineeApi {
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final Counter counter;
    private final Timer timer;

    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody TraineeRegistrationRequest registrationDto) {
        counter.increment();
        return timer.record(() -> traineeService.create(registrationDto));
    }

    @Override
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@Valid @RequestBody UserCredentialsDto auth) {
        return traineeService.login(auth);
    }

    @Override
    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@Valid @RequestBody ChangeLoginRequest changeLoginRequestDto) {
        traineeService.changePassword(changeLoginRequestDto);
    }

    @Override
    @GetMapping("/{username}")
    public TraineeWithListDto getProfile(
            @Valid @RequestBody UserCredentialsDto auth,
            @Parameter(description = "Username of trainee to fetch") @PathVariable("username") String username) {
        return traineeService.findByUsername(auth, username);
    }

    @Override
    @PutMapping("/{username}")
    public TraineeWithListDto update(@Valid @RequestBody TraineeUpdateRequest request) {
        return traineeService.update(request.getUserCredentialsDto(), request.getTraineeDto());
    }

    @Override
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody UserCredentialsDto auth) {
        traineeService.deleteTraineeByUsername(auth);
    }

    @Override
    @GetMapping("/{username}/not-assigned-trainers")
    public List<TrainerShortDto> getNotMineTrainers(@Valid @RequestBody UserCredentialsDto loginDto) {
        return traineeService.getNotMineTrainersByUsername(loginDto);
    }

    @Override
    @PutMapping("/{username}/trainers-list")
    public List<TrainerShortDto> updateTrainersList(@Valid @RequestBody UpdateTrainersRequest request) {
        return traineeService.updateTrainersList(request.getLoginDto(), request.getUpdateUsernames());
    }

    @Override
    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleActivate(@Valid @RequestBody UserCredentialsDto auth) {
        traineeService.switchActivate(auth);
    }

    @Override
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