package org.epam.service;

import org.epam.data.TraineeDao;
import org.epam.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(1L, "John", "Doe", null, null, true, new Date(), "123 Street");
    }

    @Test
    void save_ShouldGenerateUsernameAndPassword_AndCallDaoSave() {
        Set<String> existingUsernames = new HashSet<>();

        when(traineeDao.existsByUsername(anyString()))
                .thenAnswer(invocation -> existingUsernames.contains(invocation.getArgument(0)));

        doAnswer(invocation -> {
            Trainee t = invocation.getArgument(0);
            existingUsernames.add(t.getUsername());
            return null;
        }).when(traineeDao).save(any(Trainee.class));


        traineeService.create(trainee);
        assertEquals("John.Doe", trainee.getUsername());
        assertNotNull(trainee.getPassword());
        assertEquals(10, trainee.getPassword().length());
        assertFalse(trainee.getPassword().isEmpty());

        traineeService.create(trainee);
        assertEquals("John.Doe1", trainee.getUsername());

        traineeService.create(trainee);
        assertEquals("John.Doe2", trainee.getUsername());

        verify(traineeDao, times(3)).save(trainee);
    }


    @Test
    void findById_ShouldReturnTrainee_WhenExists() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> found = traineeService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(trainee, found.get());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(traineeDao.findById(999L)).thenReturn(Optional.empty());

        Optional<Trainee> found = traineeService.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        List<Trainee> trainees = Arrays.asList(trainee, new Trainee(2L, "Jane", "Doe", "janedoe", "password", true, new Date(), "456 Avenue"));

        when(traineeDao.findAll()).thenReturn(trainees);

        Collection<Trainee> result = traineeService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(trainees));
    }

    @Test
    void deleteById_ShouldCallDaoDeleteById() {
        traineeService.deleteById(1L);
        verify(traineeDao, times(1)).deleteById(1L);
    }

    @Test
    void update_ShouldCallDaoUpdate() {
        when(traineeDao.update(trainee)).thenReturn(trainee);

        Trainee updated = traineeService.update(trainee);

        assertEquals(trainee, updated);
        verify(traineeDao, times(1)).update(trainee);
    }
}
