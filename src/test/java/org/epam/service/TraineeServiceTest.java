package org.epam.service;

import org.epam.data.TraineeDao;
import org.epam.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
            MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldCallDaoSave() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        traineeService.save(trainee);
        verify(traineeDao, times(1)).save(trainee);
//        ыьвдыл?
    }

    @Test
    void findById_ShouldReturnTrainee_WhenExists() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> found = traineeService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("jdoe", found.get().getUsername());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(traineeDao.findById(999L)).thenReturn(Optional.empty());

        Optional<Trainee> found = traineeService.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        Trainee trainee1 = new Trainee(1L, "John", "Doe", "jdoe", "password", true, new Date(), "123 Street");
        Trainee trainee2 = new Trainee(2L, "Jane", "Doe", "janedoe", "password", true, new Date(), "456 Avenue");
        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);

        when(traineeDao.findAll()).thenReturn(trainees);

        Collection<Trainee> result = traineeService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void deleteById_ShouldCallDaoDeleteById() {
        traineeService.deleteById(1L);
        verify(traineeDao, times(1)).deleteById(1L);
    }
}
