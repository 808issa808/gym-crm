package org.epam.web.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.epam.web.controller.swaggerInterface.TrainerApi;
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
public class TrainerController implements TrainerApi {
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody TrainerRegistrationRequest registrationDto) {
        return trainerService.create(registrationDto);
    }

    @Override
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@Valid @RequestBody UserCredentialsDto auth) {
        return trainerService.login(auth);
    }

    @Override
    @PutMapping("/change-login")
    @ResponseStatus(HttpStatus.OK)
    public void changeLogin(@Valid @RequestBody ChangeLoginRequest changeLoginRequestDto) {
        trainerService.changePassword(changeLoginRequestDto);
    }

    @Override
    @GetMapping("/{username}")
    public TrainerWithListDto getProfile(
            @Valid @RequestBody UserCredentialsDto auth,
            @Parameter(description = "Username of trainer to fetch") @PathVariable("username") String searched) {
        return trainerService.findByUsername(auth, searched);
    }

    @Override
    @PutMapping("/{username}")
    public TrainerWithListDto update(@Valid @RequestBody TrainerUpdateRequest request) {
        return trainerService.update(request.getAuth(), request.getTrainerDto());
    }

    @Override
    @PatchMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleActivate(@Valid @RequestBody UserCredentialsDto auth) {
        trainerService.switchActivate(auth);
    }

    @Override
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