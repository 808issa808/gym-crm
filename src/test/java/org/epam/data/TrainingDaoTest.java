package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {
    private TrainingDao trainingDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        trainingDao = new TrainingDao(storage);
    }

    @Test
    void save_ShouldStoreTraining() {
        Trainer trainer = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, "Fitness");
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        Training training = new Training("Morning Workout", "Cardio", new Date(), Duration.ofHours(1), trainer, trainee);

        trainingDao.save(training);

        assertEquals(1, storage.getTrainings().size());
        assertTrue(storage.getTrainings().containsKey(trainee.getUserId()));
    }

    @Test
    void findByTraineeId_ShouldReturnTraining_WhenExists() {
        Trainer trainer = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, "Fitness");
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        Training training = new Training("Morning Workout", "Cardio", new Date(), Duration.ofHours(1), trainer, trainee);

        trainingDao.save(training);

        Optional<Training> found = trainingDao.findByTraineeId(1L);
        assertTrue(found.isPresent());
        assertEquals("Morning Workout", found.get().getName());
    }

    @Test
    void findByTraineeId_ShouldReturnEmpty_WhenNotExists() {
        Optional<Training> found = trainingDao.findByTraineeId(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainings() {
        Trainer trainer1 = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, "Fitness");
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        Training training1 = new Training("Morning Workout", "Cardio", new Date(), Duration.ofHours(1), trainer1, trainee1);

        Trainer trainer2 = new Trainer(2L, "Bob", "Johnson", "bjohnson", "password", true, "Yoga");
        Trainee trainee2 = new Trainee(2L, "Jane", "Doe", "janed", "password", true, new Date(), "456 Avenue");
        Training training2 = new Training("Evening Yoga", "Yoga", new Date(), Duration.ofHours(1), trainer2, trainee2);

        trainingDao.save(training1);
        trainingDao.save(training2);

        Collection<Training> trainings = trainingDao.findAll();
        assertEquals(2, trainings.size());
    }
}
