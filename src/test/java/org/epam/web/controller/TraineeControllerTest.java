package org.epam.web.controller;

import org.epam.service.TraineeService;
import org.epam.service.TrainingService;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.TraineeTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.*;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeController traineeController;

    @Test
    void register_ShouldReturnCredentials() {
        // Arrange
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        UserCredentialsDto expectedResponse = new UserCredentialsDto("username", "password");

        when(traineeService.create(request)).thenReturn(expectedResponse);

        // Act
        UserCredentialsDto response = traineeController.register(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(traineeService).create(request);
    }

    @Test
    void login_ShouldCallService() {
        // Arrange
        UserCredentialsDto credentials = new UserCredentialsDto("user", "pass");

        // Act
        traineeController.login(credentials);

        // Assert
        verify(traineeService).login(credentials);
    }

    @Test
    void changeLogin_ShouldCallService() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest("user", "oldPass", "newPass");

        // Act
        traineeController.changeLogin(request);

        // Assert
        verify(traineeService).changePassword(request);
    }

    @Test
    void getProfile_ShouldReturnTraineeProfile() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("user", "pass");
        String username = "trainee1";
        TraineeWithListDto expectedProfile = new TraineeWithListDto();

        when(traineeService.findByUsername(auth, username)).thenReturn(expectedProfile);

        // Act
        TraineeWithListDto response = traineeController.getProfile(auth, username);

        // Assert
        assertEquals(expectedProfile, response);
        verify(traineeService).findByUsername(auth, username);
    }

    @Test
    void update_ShouldReturnUpdatedProfile() {
        // Arrange
        TraineeUpdateRequest request = new TraineeUpdateRequest();
        request.setUserCredentialsDto(new UserCredentialsDto("user", "pass"));
        TraineeWithListDto expectedResponse = new TraineeWithListDto();

        when(traineeService.update(request.getUserCredentialsDto(), request.getTraineeDto()))
                .thenReturn(expectedResponse);

        // Act
        TraineeWithListDto response = traineeController.update(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(traineeService).update(request.getUserCredentialsDto(), request.getTraineeDto());
    }

    @Test
    void delete_ShouldCallService() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("user", "pass");

        // Act
        traineeController.delete(auth);

        // Assert
        verify(traineeService).deleteTraineeByUsername(auth);
    }

    @Test
    void getNotMineTrainers_ShouldReturnTrainersList() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("user", "pass");
        List<TrainerShortDto> expectedTrainers = Collections.singletonList(new TrainerShortDto());

        when(traineeService.getNotMineTrainersByUsername(auth)).thenReturn(expectedTrainers);

        // Act
        List<TrainerShortDto> response = traineeController.getNotMineTrainers(auth);

        // Assert
        assertEquals(expectedTrainers, response);
        verify(traineeService).getNotMineTrainersByUsername(auth);
    }

    @Test
    void updateTrainersList_ShouldReturnUpdatedList() {
        // Arrange
        UpdateTrainersRequest request = new UpdateTrainersRequest();
        request.setLoginDto(new UserCredentialsDto("user", "pass"));
        List<TrainerShortDto> expectedResponse = Collections.singletonList(new TrainerShortDto());

        when(traineeService.updateTrainersList(request.getLoginDto(), request.getUpdateUsernames()))
                .thenReturn(expectedResponse);

        // Act
        List<TrainerShortDto> response = traineeController.updateTrainersList(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(traineeService).updateTrainersList(request.getLoginDto(), request.getUpdateUsernames());
    }

    @Test
    void toggleActivate_ShouldCallService() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("user", "pass");

        // Act
        traineeController.toggleActivate(auth);

        // Assert
        verify(traineeService).switchActivate(auth);
    }

    @Test
    void findByTraineeUsernameCriteria_ShouldReturnTrainings() {
        // Arrange
        TraineeTrainingsRequest request = new TraineeTrainingsRequest();
        request.setAuth(new UserCredentialsDto("user", "pass"));
        String username = "trainee1";
        List<GetTraineeTrainingsResponse> expectedTrainings = Collections.singletonList(new GetTraineeTrainingsResponse());

        when(trainingService.findTrainingsForTrainee(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainer(),
                request.getTrainingType()))
                .thenReturn(expectedTrainings);

        // Act
        List<GetTraineeTrainingsResponse> response = traineeController.findByTraineeUsernameCriteria(request, username);

        // Assert
        assertEquals(expectedTrainings, response);
        verify(trainingService).findTrainingsForTrainee(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainer(),
                request.getTrainingType());
    }
}