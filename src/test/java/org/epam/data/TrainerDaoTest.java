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
        trainerDao = new TrainerDao();
        trainerDao.setStorage(storage);
    }

    @Test
    void save_ShouldStoreTrainerWithGeneratedId() {
        Trainer trainer = new Trainer(null, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        trainerDao.save(trainer);

        assertNotNull(trainer.getUserId());
        assertEquals(1L, trainer.getUserId());
        assertEquals(1, storage.getTrainers().size());
        assertTrue(storage.getTrainers().containsKey(trainer.getUserId()));
        assertEquals(1L, storage.getTrainerUsernameToId().get("asmith"));
    }

    @Test
    void findById_ShouldReturnTrainer_WhenExists() {
        Trainer trainer = new Trainer(null, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        trainerDao.save(trainer);

        Optional<Trainer> found = trainerDao.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("asmith", found.get().getUsername());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Trainer> found = trainerDao.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllTrainers() {
        Trainer trainer1 = new Trainer(null, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness"));
        Trainer trainer2 = new Trainer(null, "Bob", "Johnson", "bjohnson", "password", true, new TrainingType("Yoga"));
        trainerDao.save(trainer1);
        trainerDao.save(trainer2);

        Collection<Trainer> trainers = trainerDao.findAll();
        assertEquals(2, trainers.size());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenTrainerExists() {
        trainerDao.save(new Trainer(null, "Alice", "Smith", "asmith", "password", true, new TrainingType("Fitness")));

        assertTrue(trainerDao.existsByUsername("asmith"));
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenTrainerDoesNotExist() {
        assertFalse(trainerDao.existsByUsername("nonexistent"));
    }
}
