package org.epam.service;

import jakarta.persistence.EntityManager;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.model.*;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainee.*;
import org.epam.web.dto.users.trainer.TrainerShortDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TraineeService.class)
@ActiveProfiles("test")
@Transactional
class TraineeServiceComponentTest {

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TraineeRepositoryImpl traineeRepository;
    @Autowired
    private TrainerRepositoryImpl trainerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;

    @BeforeEach
    void setUp() {
        testTrainingType = new TrainingType();
        testTrainingType.setTrainingTypeName("Fitness");
        entityManager.persist(testTrainingType);

        testTrainer = new Trainer();
        testTrainer.setFirstName("Trainer");
        testTrainer.setLastName("Smith");
        testTrainer.setUsername("trainer.smith");
        testTrainer.setPassword(passwordEncoder.encode("password"));
        testTrainer.setActive(true);
        testTrainer.setSpecialization(testTrainingType);
        trainerRepository.save(testTrainer);

        testTrainee = new Trainee();
        testTrainee.setFirstName("John");
        testTrainee.setLastName("Doe");
        testTrainee.setUsername("john.doe");
        testTrainee.setPassword(passwordEncoder.encode("password"));
        testTrainee.setActive(true);
        testTrainee.setDateOfBirth(new Date());
        testTrainee.setAddress("123 Main St");
        traineeRepository.save(testTrainee);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testTrainee.getUsername(), testTrainee.getPassword(), Collections.emptyList()));
    }

    @Test
    void create_whenValidInput_shouldReturnTokenAndPersistTrainee() {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName("Alice");
        request.setLastName("Johnson");
        request.setDateOfBirth(new Date());
        request.setAddress("456 Oak Ave");

        String token = traineeService.create(request);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Trainee created = traineeRepository.findByUsername("Alice.Johnson").orElseThrow(() -> new AssertionError("Trainee not created"));

        assertEquals("Alice", created.getFirstName());
        assertEquals("Johnson", created.getLastName());
        assertEquals("456 Oak Ave", created.getAddress());
        assertTrue(created.isActive());
    }

    @Test
    void login_whenValidCredentials_shouldReturnJwtToken() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername(testTrainee.getUsername());
        credentials.setPassword("password");

        String token = traineeService.login(credentials);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void login_whenInvalidPassword_shouldThrowAuthenticationException() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername(testTrainee.getUsername());
        credentials.setPassword("wrongpassword");

        assertThrows(AuthenticationException.class, () -> traineeService.login(credentials));
    }

    @Test
    void changePassword_whenValidRequest_shouldUpdatePassword() {
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUsername(testTrainee.getUsername());
        request.setPassword("password");
        request.setNewPassword("newSecret");

        Trainee updated = traineeService.changePassword(request);

        assertTrue(passwordEncoder.matches("newSecret", updated.getPassword()));
    }

    @Test
    void update_whenValidUpdateDto_shouldUpdateTraineeProfile() {
        TraineeDto updateDto = new TraineeDto();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("Name");
        updateDto.setDateOfBirth(new Date());
        updateDto.setAddress("New Address");
        updateDto.setActive(false);

        TraineeWithListDto updated = traineeService.update(updateDto);

        assertEquals("Updated", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertEquals("New Address", updated.getAddress());
        assertFalse(updated.isActive());
    }

    @Test
    void findByUsername_whenExists_shouldReturnTraineeDto() {
        TraineeWithListDto found = traineeService.findByUsername(testTrainee.getUsername());

        assertEquals(testTrainee.getFirstName(), found.getFirstName());
        assertEquals(testTrainee.getLastName(), found.getLastName());
    }

    @Test
    void findByUsername_whenNotExists_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> traineeService.findByUsername("unknown.user"));
    }

    @Test
    void getNotMineTrainersByUsername_whenNoTrainerAssigned_shouldReturnAll() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername(testTrainee.getUsername());
        credentials.setPassword("password");

        List<TrainerShortDto> trainers = traineeService.getNotMineTrainersByUsername(credentials);

        assertFalse(trainers.isEmpty());
        assertEquals(testTrainer.getUsername(), trainers.get(0).getUsername());
    }

    @Test
    void switchActivate_shouldToggleActiveFlag() {
        boolean initialStatus = testTrainee.isActive();

        traineeService.switchActivate();

        Trainee updated = traineeRepository.findByUsername(testTrainee.getUsername()).orElseThrow();
        assertEquals(!initialStatus, updated.isActive());
    }

    @Test
    void deleteTraineeByUsername_shouldDeleteRecord() {
        traineeService.deleteTraineeByUsername();

        assertFalse(traineeRepository.findByUsername(testTrainee.getUsername()).isPresent());
    }

    @Test
    void deleteTraineeByUsername_whenAlreadyDeleted_shouldThrowException() {
        traineeService.deleteTraineeByUsername();

        assertThrows(NoSuchElementException.class, () -> traineeService.deleteTraineeByUsername());
    }
}
