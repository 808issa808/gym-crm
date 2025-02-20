package org.epam.data;

import org.epam.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoTest {
    private TraineeDao traineeDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        traineeDao = new TraineeDao(storage);
    }

    @Test
    void save_ShouldStoreTrainee() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        assertEquals(1, storage.getTrainees().size());
        assertTrue(storage.getTrainees().containsKey(trainee.getUserId()));
    }

    @Test
    void findById_ShouldReturnTrainee_WhenExists() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        Optional<Trainee> found = traineeDao.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("jdoe", found.get().getUsername());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Trainee> found = traineeDao.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        Trainee trainee2 = new Trainee(2L, "Jane", "Doe", "janedoe", "password", true, new Date(), "456 Avenue");
        traineeDao.save(trainee1);
        traineeDao.save(trainee2);

        Collection<Trainee> trainees = traineeDao.findAll();
        assertEquals(2, trainees.size());
    }
}
