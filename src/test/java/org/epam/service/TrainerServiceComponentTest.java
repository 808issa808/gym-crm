package org.epam.service;

import jakarta.persistence.EntityManager;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingTypeRepository;
import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.epam.web.dto.users.ChangeLoginRequest;
import org.epam.web.dto.users.UserCredentialsDto;
import org.epam.web.dto.users.trainer.TrainerDto;
import org.epam.web.dto.users.trainer.TrainerRegistrationRequest;
import org.epam.web.dto.users.trainer.TrainerWithListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TrainerService.class)
@ActiveProfiles("test")
@Transactional
class TrainerServiceComponentTest {

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainerRepositoryImpl trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    private Trainer testTrainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");
        entityManager.persist(trainingType);

        testTrainer = new Trainer();
        testTrainer.setFirstName("Jane");
        testTrainer.setLastName("Doe");
        testTrainer.setUsername("jane.doe");
        testTrainer.setPassword(passwordEncoder.encode("secret"));
        testTrainer.setActive(true);
        testTrainer.setSpecialization(trainingType);
        trainerRepository.save(testTrainer);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(testTrainer.getUsername(), testTrainer.getPassword(), Collections.emptyList()));
    }

    @Test
    void create_shouldRegisterTrainerAndReturnToken() {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName("Mike");
        request.setLastName("Tyson");
        request.setSpecialization(trainingType.getId());

        String token = trainerService.create(request);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        boolean exists = trainerRepository.findByUsername("Mike.Tyson").isPresent();
        assertTrue(exists);
    }

    @Test
    void login_shouldReturnJwtToken() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername(testTrainer.getUsername());
        credentials.setPassword("secret");

        String token = trainerService.login(credentials);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void login_withWrongPassword_shouldThrowException() {
        UserCredentialsDto credentials = new UserCredentialsDto();
        credentials.setUsername(testTrainer.getUsername());
        credentials.setPassword("wrong");

        assertThrows(AuthenticationException.class, () -> trainerService.login(credentials));
    }

    @Test
    void changePassword_shouldUpdatePassword() {
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUsername(testTrainer.getUsername());
        request.setPassword("secret");
        request.setNewPassword("newPassword");

        Trainer updated = trainerService.changePassword(request);

        assertTrue(passwordEncoder.matches("newPassword", updated.getPassword()));
    }

    @Test
    void update_shouldUpdateTrainerProfile() {
        TrainerDto dto = new TrainerDto();
        dto.setUsername(testTrainer.getUsername());
        dto.setFirstName("Updated");
        dto.setLastName("Trainer");
        dto.setActive(false);
        dto.setSpecialization(trainingType.getId());

        TrainerWithListDto result = trainerService.update(dto);

        assertEquals("Updated", result.getFirstName());
        assertEquals("Trainer", result.getLastName());
        assertFalse(result.isActive());
    }

    @Test
    void update_withWrongUsername_shouldThrowForbidden() {
        TrainerDto dto = new TrainerDto();
        dto.setUsername("wrong.username");
        dto.setFirstName("Test");
        dto.setLastName("Fail");
        dto.setActive(true);
        dto.setSpecialization(trainingType.getId());

        assertThrows(RuntimeException.class, () -> trainerService.update(dto));
    }

    @Test
    void findByUsername_shouldReturnTrainerDto() {
        TrainerWithListDto dto = trainerService.findByUsername(testTrainer.getUsername());

        assertEquals(testTrainer.getFirstName(), dto.getFirstName());
        assertEquals(testTrainer.getLastName(), dto.getLastName());
    }

    @Test
    void findByUsername_withUnknown_shouldThrowException() {
        assertThrows(NoSuchElementException.class, () -> trainerService.findByUsername("not.found"));
    }

    @Test
    void switchActivate_shouldToggleActiveStatus() {
        boolean initial = testTrainer.isActive();

        trainerService.switchActivate();

        Trainer updated = trainerRepository.findByUsername(testTrainer.getUsername()).orElseThrow();
        assertEquals(!initial, updated.isActive());
    }
}
