package org.epam.data;

import org.epam.model.Trainer;
import org.epam.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDaoTest {
    private TrainerDao trainerDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        trainerDao = new TrainerDao(storage);
    }

    @Test
    void save_ShouldStoreTrainer() {
        Trainer trainer = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        trainerDao.save(trainer);

        assertEquals(1, storage.getTrainers().size());
        assertTrue(storage.getTrainers().containsKey(trainer.getUserId()));
    }

    @Test
    void findById_ShouldReturnTrainer_WhenExists() {
        Trainer trainer = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        trainerDao.save(trainer);

        Optional<Trainer> found = trainerDao.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("asmith", found.get().getUsername());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Trainer> found = trainerDao.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainers() {
        Trainer trainer1 = new Trainer(1L, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        Trainer trainer2 = new Trainer(2L, "Bob", "Johnson", "bjohnson", "password", true, new TrainingType("Yoga"));
        trainerDao.save(trainer1);
        trainerDao.save(trainer2);

        Collection<Trainer> trainers = trainerDao.findAll();
        assertEquals(2, trainers.size());
    }
}
