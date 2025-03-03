package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Test
    void whenStorageIsEmpty_thenAllMapsAreEmpty() {
        assertTrue(storage.getTrainees().isEmpty(), "Trainees map should be empty");
        assertTrue(storage.getTrainers().isEmpty(), "Trainers map should be empty");
        assertTrue(storage.getTrainings().isEmpty(), "Trainings map should be empty");
        assertTrue(storage.getTraineeUsernameToId().isEmpty(), "Trainee username map should be empty");
        assertTrue(storage.getTrainerUsernameToId().isEmpty(), "Trainer username map should be empty");
    }

    @Test
    void whenTraineeAdded_thenStorageContainsIt() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");

        storage.getTrainees().put(1L, trainee);
        storage.getTraineeUsernameToId().put("johndoe", 1L);

        assertEquals(1, storage.getTrainees().size());
        assertEquals(trainee, storage.getTrainees().get(1L));
        assertEquals(1L, storage.getTraineeUsernameToId().get("johndoe"));
    }

    @Test
    void whenTrainerAdded_thenStorageContainsIt() {
        Trainer trainer = new Trainer(2L, "Alice", "Smith", "alicesmith", "password", true, new TrainingType("Yoga"));

        storage.getTrainers().put(2L, trainer);
        storage.getTrainerUsernameToId().put("alicesmith", 2L);

        assertEquals(1, storage.getTrainers().size());
        assertEquals(trainer, storage.getTrainers().get(2L));
        assertEquals(2L, storage.getTrainerUsernameToId().get("alicesmith"));
    }

    @Test
    void whenTrainingAdded_thenStorageContainsIt() {
        Trainer trainer = new Trainer(3L, "Bob", "Brown", "bobbrown", "password", true, new TrainingType("Boxing"));
        Trainee trainee = new Trainee(4L, "Emily", "Clark", "emilyclark", "password", true, new Date(), "456 Avenue");
        Training training = new Training("Boxing Basics", new TrainingType("Combat"), new Date(), Duration.ofHours(1), trainer, trainee);

        storage.getTrainings().put(5L, training);

        assertEquals(1, storage.getTrainings().size());
        assertEquals(training, storage.getTrainings().get(5L));
    }

    @Test
    void whenTraineeRemoved_thenStorageDoesNotContainIt() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");

        storage.getTrainees().put(1L, trainee);
        storage.getTraineeUsernameToId().put("johndoe", 1L);

        storage.getTrainees().remove(1L);
        storage.getTraineeUsernameToId().remove("johndoe");

        assertFalse(storage.getTrainees().containsKey(1L));
        assertFalse(storage.getTraineeUsernameToId().containsKey("johndoe"));
    }

    @Test
    void whenTrainerRemoved_thenStorageDoesNotContainIt() {
        Trainer trainer = new Trainer(2L, "Alice", "Smith", "alicesmith", "password", true, new TrainingType("Yoga"));

        storage.getTrainers().put(2L, trainer);
        storage.getTrainerUsernameToId().put("alicesmith", 2L);

        storage.getTrainers().remove(2L);
        storage.getTrainerUsernameToId().remove("alicesmith");

        assertFalse(storage.getTrainers().containsKey(2L));
        assertFalse(storage.getTrainerUsernameToId().containsKey("alicesmith"));
    }

    @Test
    void whenTrainingRemoved_thenStorageDoesNotContainIt() {
        Training training = new Training("Boxing Basics", new TrainingType("Combat"), new Date(), Duration.ofHours(1), null, null);

        storage.getTrainings().put(3L, training);
        storage.getTrainings().remove(3L);

        assertFalse(storage.getTrainings().containsKey(3L));
    }
}
