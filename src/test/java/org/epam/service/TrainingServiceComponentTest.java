package org.epam.service;

import jakarta.persistence.EntityManager;
import org.epam.data.impl.TraineeRepositoryImpl;
import org.epam.data.impl.TrainerRepositoryImpl;
import org.epam.data.impl.TrainingRepositoryImpl;
import org.epam.model.*;
import org.epam.service.workload.WorkloadService;
import org.epam.service.workload.dto.TrainerSummary;
import org.epam.web.dto.training.GetTraineeTrainingsResponse;
import org.epam.web.dto.training.GetTrainerTrainingsResponse;
import org.epam.web.dto.training.TrainingCreateDto;
import org.epam.web.exp.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TrainingService.class)
@ActiveProfiles("test")
@Transactional
class TrainingServiceComponentTest {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingRepositoryImpl trainingRepository;

    @Autowired
    private TraineeRepositoryImpl traineeRepository;

    @Autowired
    private TrainerRepositoryImpl trainerRepository;

    @Autowired
    private EntityManager entityManager;

    @MockBean
    private WorkloadService workloadService;

    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;
    private Training testTraining;

    @BeforeEach
    void setUp() {
        testTrainingType = new TrainingType();
        testTrainingType.setTrainingTypeName("Yoga");
        entityManager.persist(testTrainingType);

        testTrainer = new Trainer();
        testTrainer.setFirstName("Trainer");
        testTrainer.setLastName("Smith");
        testTrainer.setUsername("trainer.smith");
        testTrainer.setPassword("password");
        testTrainer.setActive(true);
        testTrainer.setSpecialization(testTrainingType);
        trainerRepository.save(testTrainer);

        testTrainee = new Trainee();
        testTrainee.setFirstName("John");
        testTrainee.setLastName("Doe");
        testTrainee.setUsername("john.doe");
        testTrainee.setPassword("password");
        testTrainee.setActive(true);
        testTrainee.setDateOfBirth(new Date());
        testTrainee.setAddress("123 Main St");
        traineeRepository.save(testTrainee);

        testTraining = new Training();
        testTraining.setTrainer(testTrainer);
        testTraining.setTrainee(testTrainee);
        testTraining.setName("Morning Yoga");
        testTraining.setType(testTrainingType);
        testTraining.setDuration(60);
        testTraining.setDate(new Date(1700000000000L));
        trainingRepository.create(testTraining);

        // Mock workload service calls
        doNothing().when(workloadService).updateTrainerSummary(any());
        when(workloadService.getSummary(any())).thenReturn(new TrainerSummary());
    }

    @Test
    void create_whenValidInput_shouldCreateTraining() {
        // Set trainer as the current user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testTrainer.getUsername(), testTrainer.getPassword()));

        TrainingCreateDto createDto = new TrainingCreateDto();
        createDto.setTrainee(testTrainee.getUsername());
        createDto.setName("Evening Yoga");
        createDto.setDate(new Date());
        createDto.setDuration(45);

        trainingService.create(createDto);

        // Verify using findByTrainerUsername instead of findAll
        List<Training> trainings = trainingRepository.findByTrainerUsername(testTrainer.getUsername());
        assertEquals(2, trainings.size()); // original + new one
    }

    @Test
    void create_whenInvalidTrainee_shouldThrowException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testTrainer.getUsername(), testTrainer.getPassword()));

        TrainingCreateDto createDto = new TrainingCreateDto();
        createDto.setTrainee("nonexistent.trainee");
        createDto.setName("Evening Yoga");
        createDto.setDate(new Date());
        createDto.setDuration(45);

        assertThrows(IllegalArgumentException.class, () -> trainingService.create(createDto));
    }

    @Test
    @Transactional
    void deleteTrainingById_whenOwner_shouldDeleteTraining() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testTrainer.getUsername(), testTrainer.getPassword()));

        // First verify the training exists
        assertTrue(trainingRepository.findTrainingById(testTraining.getId().intValue()).isPresent());

        // Delete the training
        trainingService.deleteTrainingById(testTraining.getId().intValue());

        // Flush the changes to the database within the same transaction
        entityManager.flush();
        entityManager.clear();

        // Now verify the training is gone
        assertFalse(trainingRepository.findTrainingById(testTraining.getId().intValue()).isPresent());
    }

    @Test
    void deleteTrainingById_whenNotOwner_shouldThrowForbiddenException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("another.trainer", "password"));

        assertThrows(ForbiddenException.class,
                () -> trainingService.deleteTrainingById(testTraining.getId().intValue()));
    }

    @Test
    void findTrainingsForTrainee_whenValidCriteria_shouldReturnTrainings() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testTrainee.getUsername(), testTrainee.getPassword()));

        Date trainingDate = testTraining.getDate();
        Date fromDate = new Date(trainingDate.getTime() - 1000);
        Date toDate = new Date(trainingDate.getTime() + 1000);


        List<GetTraineeTrainingsResponse> trainings = trainingService.findTrainingsForTrainee(
                fromDate, toDate, null, testTrainingType.getTrainingTypeName());

        assertEquals(1, trainings.size());
        assertEquals(testTraining.getName(), trainings.get(0).getName());
    }

    @Test
    void findTrainingsForTrainer_whenValidCriteria_shouldReturnTrainings() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testTrainer.getUsername(), testTrainer.getPassword()));

        Date trainingDate = testTraining.getDate();
        Date fromDate = new Date(trainingDate.getTime() - 1000);
        Date toDate = new Date(trainingDate.getTime() + 1000);

        List<GetTrainerTrainingsResponse> trainings = trainingService.findTrainingsForTrainer(
                fromDate, toDate, null);

        assertEquals(1, trainings.size());
        assertEquals(testTraining.getName(), trainings.get(0).getName());
    }

    @Test
    void getSummary_shouldReturnTrainerSummary() {
        TrainerSummary summary = trainingService.getSummary(testTrainer.getUsername());
        assertNotNull(summary);
    }
}