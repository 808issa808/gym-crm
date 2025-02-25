package org.epam.service;

import org.epam.data.TrainingDao;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldCallDaoSave() {
        Trainer trainer = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Fitness");
        Trainee trainee = new Trainee(2L, "Bob", "Green", "bgreen", "password", true, new Date(), "123 Main St");
        Training training = new Training("Yoga", "Group", new Date(), Duration.ofHours(1), trainer, trainee);

        trainingService.save(training);

        verify(trainingDao, times(1)).save(training);
    }

    @Test
    void findByTraineeId_ShouldReturnTraining_WhenExists() {
        Trainer trainer = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Fitness");
        Trainee trainee = new Trainee(2L, "Bob", "Green", "bgreen", "password", true, new Date(), "123 Main St");
        Training training = new Training("Yoga", "Group", new Date(), Duration.ofHours(1), trainer, trainee);

        when(trainingDao.findByTraineeId(2L)).thenReturn(Optional.of(training));

        Optional<Training> found = trainingService.findByTraineeId(2L);

        assertTrue(found.isPresent());
        assertEquals("Yoga", found.get().getName());
    }

    @Test
    void findByTraineeId_ShouldReturnEmpty_WhenNotExists() {
        when(trainingDao.findByTraineeId(999L)).thenReturn(Optional.empty());

        Optional<Training> found = trainingService.findByTraineeId(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        Trainer trainer = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Fitness");
        Trainee trainee1 = new Trainee(2L, "Bob", "Green", "bgreen", "password", true, new Date(), "123 Main St");
        Trainee trainee2 = new Trainee(3L, "Charlie", "White", "cwhite", "password", true, new Date(), "456 Elm St");
        Training training1 = new Training("Yoga", "Group", new Date(), Duration.ofHours(1), trainer, trainee1);
        Training training2 = new Training("Pilates", "Group", new Date(), Duration.ofHours(1), trainer, trainee2);

        List<Training> trainings = Arrays.asList(training1, training2);

        when(trainingDao.findAll()).thenReturn(trainings);

        Collection<Training> result = trainingService.findAll();

        assertEquals(2, result.size());
    }
}