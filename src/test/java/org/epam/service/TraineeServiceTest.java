package org.epam.service;

import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.*;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepositoryImpl traineeRepository;

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private UserCredentialsDto credentials;
    private TraineeRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        credentials = new UserCredentialsDto("john.doe", "password123");

        registrationRequest = new TraineeRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");

        traineeService.setTrainerRepository(trainerRepository);
    }

    @Test
    void create_ShouldGenerateCredentialsAndSaveTrainee() {
        // Arrange
        when(traineeRepository.countByUsernamePrefix(anyString())).thenReturn(0);
        when(modelMapper.map(registrationRequest, Trainee.class)).thenReturn(trainee);
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(modelMapper.map(trainee, UserCredentialsDto.class)).thenReturn(new UserCredentialsDto("john.doe", "generatedPass"));

        // Act
        UserCredentialsDto result = traineeService.create(registrationRequest);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void login_ShouldAuthenticateSuccessfully() {
        // Arrange
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        // Act & Assert
        assertDoesNotThrow(() -> traineeService.login(credentials));
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest("john.doe", "password123", "newPass");
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeService.changePassword(request);

        // Assert
        assertEquals("newPass", result.getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void update_ShouldUpdateTraineeProfile() {
        // Arrange
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUsername("john.doe");
        TraineeWithListDto expectedDto = new TraineeWithListDto();

        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        doNothing().when(modelMapper).map(traineeDto, trainee);
        when(modelMapper.map(trainee, TraineeWithListDto.class)).thenReturn(expectedDto);
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        // Act
        TraineeWithListDto result = traineeService.update(credentials, traineeDto);

        // Assert
        assertNotNull(result);
        verify(traineeRepository).save(trainee);
    }

    @Test
    void findByUsername_ShouldReturnTraineeProfile() {
        // Arrange
        TraineeWithListDto expectedDto = new TraineeWithListDto();
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(modelMapper.map(trainee, TraineeWithListDto.class)).thenReturn(expectedDto);

        // Act
        TraineeWithListDto result = traineeService.findByUsername(credentials, "john.doe");

        // Assert
        assertNotNull(result);
        verify(modelMapper).map(trainee, TraineeWithListDto.class);
    }

    @Test
    void getNotMineTrainersByUsername_ShouldReturnTrainersList() {
        // Arrange
        Trainer trainer = new Trainer();
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.getNotMineTrainersByUsername("john.doe")).thenReturn(List.of(trainer));

        TrainerShortDto shortDto = new TrainerShortDto();
        when(modelMapper.map(trainer, TrainerShortDto.class)).thenReturn(shortDto);

        // Act
        List<TrainerShortDto> result = traineeService.getNotMineTrainersByUsername(credentials);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(shortDto, result.get(0));
        verify(modelMapper).map(trainer, TrainerShortDto.class);
    }

    @Test
    void updateTrainersList_ShouldUpdateAssignedTrainers() {
        // Arrange
        TrainerListPutDto request = new TrainerListPutDto();
        request.setTrainerUsernameList(List.of("trainer1"));
        Trainer trainer = new Trainer();
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        when(modelMapper.map(trainer, TrainerShortDto.class)).thenReturn(new TrainerShortDto());

        // Act
        List<TrainerShortDto> result = traineeService.updateTrainersList(credentials, request);

        // Assert
        assertFalse(result.isEmpty());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void switchActivate_ShouldToggleActiveStatus() {
        // Arrange
        trainee.setActive(true);
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        // Act
        traineeService.switchActivate(credentials);

        // Assert
        assertFalse(trainee.isActive());
    }

    @Test
    void deleteTraineeByUsername_ShouldRemoveTrainee() {
        // Arrange
        when(traineeRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee);

        // Act
        traineeService.deleteTraineeByUsername(credentials);

        // Assert
        verify(traineeRepository).delete(trainee);
    }
}