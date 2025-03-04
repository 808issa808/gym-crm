package org.epam.data;

import org.epam.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoTest {

    private TraineeDao traineeDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        traineeDao = new TraineeDao();
        traineeDao.setStorage(storage);
    }

    @Test
    void givenNewTrainee_whenSave_thenTraineeIsStoredWithId() {
        Trainee trainee = new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");

        traineeDao.save(trainee);

        assertNotNull(trainee.getUserId());
        assertEquals(1L, trainee.getUserId());
        assertEquals(1, storage.getTrainees().size());
        assertEquals(1L, storage.getTraineeUsernameToId().get("johndoe"));
    }

    @Test
    void givenExistingTrainee_whenUpdate_thenTraineeIsUpdated() {
        Trainee trainee = new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        trainee.setAddress("456 Avenue");
        Trainee updatedTrainee = traineeDao.update(trainee);

        assertEquals("456 Avenue", updatedTrainee.getAddress());
        assertEquals("456 Avenue", storage.getTrainees().get(1L).getAddress());
    }

    @Test
    void givenNonExistingTrainee_whenUpdate_thenThrowException() {
        Trainee trainee = new Trainee(99L, "Jane", "Doe", "janedoe", "password", true, new Date(), "789 Street");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> traineeDao.update(trainee));
        assertEquals("Trainee with id 99 does not exist", exception.getMessage());
    }

    @Test
    void givenExistingTrainee_whenFindById_thenReturnTrainee() {
        Trainee trainee = new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        Optional<Trainee> foundTrainee = traineeDao.findById(1L);

        assertTrue(foundTrainee.isPresent());
        assertEquals("John", foundTrainee.get().getFirstName());
    }

    @Test
    void givenNonExistingTrainee_whenFindById_thenReturnEmpty() {
        Optional<Trainee> foundTrainee = traineeDao.findById(1L);
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    void givenMultipleTrainees_whenFindAll_thenReturnAll() {
        traineeDao.save(new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street"));
        traineeDao.save(new Trainee(null, "Alice", "Smith", "alicesmith", "password", true, new Date(), "456 Avenue"));

        List<Trainee> allTrainees = new ArrayList<>(traineeDao.findAll());

        assertEquals(2, allTrainees.size());
        assertEquals(1L, allTrainees.get(0).getUserId());
        assertEquals(2L, allTrainees.get(1).getUserId());
    }

    @Test
    void givenExistingTrainee_whenDeleteById_thenTraineeIsRemoved() {
        Trainee trainee = new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        traineeDao.deleteById(1L);

        assertFalse(storage.getTrainees().containsKey(1L));
        assertFalse(storage.getTraineeUsernameToId().containsKey("johndoe"));
    }

    @Test
    void givenUsername_whenExistsByUsername_thenReturnTrue() {
        traineeDao.save(new Trainee(null, "John", "Doe", "johndoe", "password", true, new Date(), "123 Street"));

        assertTrue(traineeDao.existsByUsername("johndoe"));
    }

    @Test
    void givenUsername_whenExistsByUsername_thenReturnFalse() {
        assertFalse(traineeDao.existsByUsername("nonexistent"));
    }
    @Test
    void deleteById_ShouldRemoveTrainee_WhenExists() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        traineeDao.deleteById(1L);

        assertEquals(0, storage.getTrainees().size());
        assertTrue(traineeDao.findById(1L).isEmpty());
    }

    @Test
    void deleteById_ShouldDoNothing_WhenNotExists() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        traineeDao.save(trainee);

        assertThrows(IllegalArgumentException.class,()-> traineeDao.deleteById(999L)); // ID не существует

        assertEquals(1, storage.getTrainees().size());
        assertTrue(traineeDao.findById(1L).isPresent());
    }
}
