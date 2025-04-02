package org.epam.service;

import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainer.TrainerDto;
import org.epam.web.dto.users.trainer.TrainerRegistrationRequest;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.epam.web.exp.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepositoryImpl trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private UserCredentialsDto credentials;
    private TrainerRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setUsername("john.doe");
        trainer.setPassword("password123");
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setActive(true);

        credentials = new UserCredentialsDto("john.doe", "password123");

        registrationRequest = new TrainerRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setSpecialization(1L);
    }

    @Test
    void create_ShouldGenerateCredentialsAndSaveTrainer() {
        // Arrange
        TrainingType specialization = new TrainingType();
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(specialization));
        when(trainerRepository.countByUsernamePrefix(anyString())).thenReturn(0);
        when(modelMapper.map(registrationRequest, Trainer.class)).thenReturn(trainer);
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(modelMapper.map(trainer, UserCredentialsDto.class)).thenReturn(new UserCredentialsDto("john.doe", "generatedPass"));

        // Act
        UserCredentialsDto result = trainerService.create(registrationRequest);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        verify(trainerRepository).save(trainer);
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    void create_ShouldThrowException_WhenSpecializationNotFound() {
        // Arrange
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> trainerService.create(registrationRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Specialization not found", exception.getReason());
    }

    @Test
    void login_ShouldAuthenticateSuccessfully() {
        // Arrange
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

        // Act & Assert
        assertDoesNotThrow(() -> trainerService.login(credentials));
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest("john.doe", "password123", "newPass");
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        // Act
        Trainer result = trainerService.changePassword(request);

        // Assert
        assertEquals("newPass", result.getPassword());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void findByUsername_ShouldReturnTrainerProfile() {
        // Arrange
        TrainerWithListDto expectedDto = new TrainerWithListDto();
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(modelMapper.map(trainer, TrainerWithListDto.class)).thenReturn(expectedDto);

        // Act
        TrainerWithListDto result = trainerService.findByUsername(credentials, "john.doe");

        // Assert
        assertNotNull(result);
        verify(modelMapper).map(trainer, TrainerWithListDto.class);
    }

    @Test
    void findByUsername_ShouldThrowException_WhenTrainerNotFound() {
        // Arrange
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> trainerService.findByUsername(credentials, "unknown"));
        assertEquals("There is no trainer with username: unknown", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainerProfile() {
        // Arrange
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUsername("john.doe");
        trainerDto.setSpecialization(1L);
        TrainerWithListDto expectedDto = new TrainerWithListDto();
        TrainingType specialization = new TrainingType();

        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(specialization));
        doNothing().when(modelMapper).map(trainerDto, trainer);
        when(modelMapper.map(trainer, TrainerWithListDto.class)).thenReturn(expectedDto);
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        // Act
        TrainerWithListDto result = trainerService.update(credentials, trainerDto);

        // Assert
        assertNotNull(result);
        verify(trainerRepository).save(trainer);
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    void update_ShouldThrowForbidden_WhenUpdatingOtherUser() {
        // Arrange
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUsername("other.user");
        trainerDto.setSpecialization(1L);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> trainerService.update(credentials, trainerDto));
        assertEquals("Нельзя изменить данные другого пользователя", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(trainerRepository);
        verifyNoInteractions(trainingTypeRepository);
    }

    @Test
    void switchActivate_ShouldToggleActiveStatus() {
        // Arrange
        trainer.setActive(true);
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        // Act
        trainerService.switchActivate(credentials);

        // Assert
        assertFalse(trainer.isActive());
        verify(trainerRepository).save(trainer);
    }
}