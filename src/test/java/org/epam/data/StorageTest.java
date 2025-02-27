package org.epam.data;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.epam.model.Training;
import org.epam.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @Test
    void givenEmptyStorage_whenRetrieveEntities_thenReturnEmptyMaps() {
        Storage storage = new Storage();

        assertTrue(storage.getTrainees().isEmpty());
        assertTrue(storage.getTrainers().isEmpty());
        assertTrue(storage.getTrainings().isEmpty());
    }

    @Test
    void givenStorage_whenAddTrainee_thenTraineeIsStored() {
        Storage storage = new Storage();
        Trainee trainee = new Trainee(1L, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");

        storage.getTrainees().put(1L, trainee);

        assertEquals(1, storage.getTrainees().size());
        assertEquals(trainee, storage.getTrainees().get(1L));
    }

    @Test
    void givenStorage_whenAddTrainer_thenTrainerIsStored() {
        Storage storage = new Storage();
        Trainer trainer = new Trainer(2L, "Alice", "Smith", "alicesmith", "password", true, new TrainingType("Yoga"));

        storage.getTrainers().put(2L, trainer);

        assertEquals(1, storage.getTrainers().size());
        assertEquals(trainer, storage.getTrainers().get(2L));
    }


    @Test
    void givenStorage_whenAddTraining_thenTrainingIsStored() {
        Storage storage = new Storage();
        Trainer trainer = new Trainer(3L, "Bob", "Brown", "bobbrown", "password", true, new TrainingType("Boxing"));
        Trainee trainee = new Trainee(4L, "Emily", "Clark", "emilyclark", "password", true, new Date(), "456 Avenue");
        Training training = new Training("Boxing Basics", new TrainingType("Combat"), new Date(), Duration.ofHours(1), trainer, trainee);

        storage.getTrainings().put(5L, training);

        assertEquals(1, storage.getTrainings().size());
        assertEquals(training, storage.getTrainings().get(5L));
    }
}
