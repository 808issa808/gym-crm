package org.epam.data.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Trainee> traineeQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @InjectMocks
    private TraineeRepositoryImpl traineeRepository;

    @Test
    void testExistsByUsername() {
        String username = "testUser";

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter("username", username)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean exists = traineeRepository.existsByUsername(username);

        assertTrue(exists);
        verify(entityManager).createQuery(anyString(), eq(Long.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);

        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(List.of(trainee).stream());

        Optional<Trainee> result = traineeRepository.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    void testFindByUsername_UserNotExists() {
        String username = "testUser";

        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.getResultStream()).thenReturn(Stream.empty());

        Optional<Trainee> result = traineeRepository.findByUsername(username);

        assertFalse(result.isPresent());
    }


    @Test
    void testCreate() {
        Trainee trainee = new Trainee();
        trainee.setUsername("newUser");

        traineeRepository.create(trainee);

        verify(entityManager).persist(trainee);
    }

    @Test
    void testUpdate() {
        Trainee trainee = new Trainee();
        trainee.setUsername("updatedUser");

        when(entityManager.merge(trainee)).thenReturn(trainee);

        Trainee result = traineeRepository.update(trainee);

        assertNotNull(result);
        assertEquals(trainee.getUsername(), result.getUsername());
    }

    @Test
    void testChangePassword() {
        Trainee trainee = new Trainee();
        trainee.setUsername("user");
        trainee.setPassword("oldPassword");

        String newPassword = "newPassword";
        when(entityManager.merge(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeRepository.changePassword(trainee, newPassword);

        assertEquals(newPassword, result.getPassword());
        verify(entityManager).merge(trainee);
    }
    @Test
    void testGetNotMineTrainersByUsername() {
        String username = "testUser";
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());

        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(trainers);

        List<Trainer> result = traineeRepository.getNotMineTrainersByUsername(username);

        assertEquals(2, result.size());
        verify(entityManager).createQuery(anyString(), eq(Trainer.class));
    }
}