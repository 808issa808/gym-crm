package org.epam.web.controller;

import org.epam.service.TrainerService;
import org.epam.service.TrainingService;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainerTrainingsRequest;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainer.*;
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
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void register_ShouldReturnCredentials() {
        // Arrange
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        UserCredentialsDto expectedResponse = new UserCredentialsDto("trainer", "password");

        when(trainerService.create(request)).thenReturn(expectedResponse);

        // Act
        UserCredentialsDto response = trainerController.register(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(trainerService).create(request);
    }

    @Test
    void login_ShouldCallService() {
        // Arrange
        UserCredentialsDto credentials = new UserCredentialsDto("trainer", "pass");

        // Act
        trainerController.login(credentials);

        // Assert
        verify(trainerService).login(credentials);
    }

    @Test
    void changeLogin_ShouldCallService() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest("trainer", "oldPass", "newPass");

        // Act
        trainerController.changeLogin(request);

        // Assert
        verify(trainerService).changePassword(request);
    }

    @Test
    void getProfile_ShouldReturnTrainerProfile() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("trainer", "pass");
        String username = "trainer1";
        TrainerWithListDto expectedProfile = new TrainerWithListDto();

        when(trainerService.findByUsername(auth, username)).thenReturn(expectedProfile);

        // Act
        TrainerWithListDto response = trainerController.getProfile(auth, username);

        // Assert
        assertEquals(expectedProfile, response);
        verify(trainerService).findByUsername(auth, username);
    }

    @Test
    void update_ShouldReturnUpdatedProfile() {
        // Arrange
        TrainerUpdateRequest request = new TrainerUpdateRequest();
        request.setAuth(new UserCredentialsDto("trainer", "pass"));
        TrainerWithListDto expectedResponse = new TrainerWithListDto();

        when(trainerService.update(request.getAuth(), request.getTrainerDto()))
                .thenReturn(expectedResponse);

        // Act
        TrainerWithListDto response = trainerController.update(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(trainerService).update(request.getAuth(), request.getTrainerDto());
    }

    @Test
    void toggleActivate_ShouldCallService() {
        // Arrange
        UserCredentialsDto auth = new UserCredentialsDto("trainer", "pass");

        // Act
        trainerController.toggleActivate(auth);

        // Assert
        verify(trainerService).switchActivate(auth);
    }

    @Test
    void findByTrainerUsernameCriteria_ShouldReturnTrainings() {
        // Arrange
        TrainerTrainingsRequest request = new TrainerTrainingsRequest();
        request.setAuth(new UserCredentialsDto("trainer", "pass"));
        String username = "trainer1";
        List<GetTrainerTrainingsResponse> expectedTrainings = Collections.singletonList(new GetTrainerTrainingsResponse());

        when(trainingService.findTrainingsForTrainer(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainee()))
                .thenReturn(expectedTrainings);

        // Act
        List<GetTrainerTrainingsResponse> response = trainerController.findByTrainerUsernameCriteria(request, username);

        // Assert
        assertEquals(expectedTrainings, response);
        verify(trainingService).findTrainingsForTrainer(
                request.getAuth().getUsername(),
                request.getAuth().getPassword(),
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainee());
    }
}