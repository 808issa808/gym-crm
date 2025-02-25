package org.epam.service;

import org.epam.data.TrainerDao;
import org.epam.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldCallDaoSave() {
        Trainer trainer = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Java");
        trainerService.save(trainer);
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void findById_ShouldReturnTrainer_WhenExists() {
        Trainer trainer = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Java");
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> found = trainerService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("abrown", found.get().getUsername());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(trainerDao.findById(999L)).thenReturn(Optional.empty());

        Optional<Trainer> found = trainerService.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainers() {
        Trainer trainer1 = new Trainer(1L, "Alice", "Brown", "abrown", "password", true, "Java");
        Trainer trainer2 = new Trainer(2L, "Bob", "Green", "bgreen", "password", false, "Python");
        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);

        when(trainerDao.findAll()).thenReturn(trainers);

        Collection<Trainer> result = trainerService.findAll();

        assertEquals(2, result.size());
    }
}
